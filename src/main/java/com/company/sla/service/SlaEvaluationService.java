package com.company.sla.service;

import com.company.sla.dto.EvaluationRequest;
import com.company.sla.dto.EvaluationResponse;
import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaEvaluationLog;
import com.company.sla.repository.SlaConfigurationRepository;
import com.company.sla.repository.SlaEvaluationLogRepository;
import com.company.sla.service.engine.RuleEngine;
import com.company.sla.dto.EvaluationResultDto;
import com.company.sla.service.sla.SLA;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SlaEvaluationService {
    
    private static final Logger log = LoggerFactory.getLogger(SlaEvaluationService.class);

    private final SlaConfigurationRepository slaConfigurationRepository;
    private final SlaEvaluationLogRepository evaluationLogRepository;
    private final RuleEngine ruleEngine;
    private final ObjectMapper objectMapper;
    private final List<SLA<?, ?>> slaImplementations;

    public SlaEvaluationService(SlaConfigurationRepository slaConfigurationRepository,
                                SlaEvaluationLogRepository evaluationLogRepository,
                                RuleEngine ruleEngine,
                                ObjectMapper objectMapper,
                                List<SLA<?, ?>> slaImplementations) {
        this.slaConfigurationRepository = slaConfigurationRepository;
        this.evaluationLogRepository = evaluationLogRepository;
        this.ruleEngine = ruleEngine;
        this.objectMapper = objectMapper;
        this.slaImplementations = slaImplementations;
    }

    public EvaluationResponse evaluate(Long slaId, EvaluationRequest request) {
        SlaConfiguration config = slaConfigurationRepository.findById(slaId)
                .orElseThrow(() -> new RuntimeException("SLA not found"));

        if (!config.isActive()) {
            throw new RuntimeException("SLA is not active");
        }

        try {
            // 1. Resolve Context Type
            Class<?> contextClass = Class.forName("com.company.sla.model.context." + config.getContextType());
            Object contextObj = objectMapper.convertValue(request.getContext(), contextClass);

            // 2. Calculate Weight & Result
            EvaluationResultDto engineResult = ruleEngine.determineResult(config, contextObj);
            String result = (engineResult != null) ? engineResult.getResult() : null;
            
            // Score from engine (Sum of matched field weights)
            BigDecimal totalWeight = engineResult != null ? BigDecimal.valueOf(engineResult.getScore()) : BigDecimal.ZERO;

            // 3. Log Evaluation
            SlaEvaluationLog logEntry = new SlaEvaluationLog();
            logEntry.setSlaConfiguration(config);
            logEntry.setContextJson(objectMapper.writeValueAsString(request.getContext()));
            logEntry.setTotalWeight(totalWeight);
            logEntry.setResultValue(result);
            evaluationLogRepository.save(logEntry);

            // Determine the 'result' boolean:
            // - For BOOLEAN type: parse the resultValue
            // - For Entity types: true if a rule matched, false otherwise
            boolean booleanResult;
            if ("BOOLEAN".equalsIgnoreCase(config.getResultType())) {
                booleanResult = Boolean.parseBoolean(result);
            } else {
                booleanResult = (engineResult != null && result != null);
            }

            return EvaluationResponse.builder()
                    .result(booleanResult)
                    .resultValue(result)
                    .totalWeight(totalWeight)
                    .slaName(config.getSlaName())
                    .matchedRuleName(engineResult != null ? engineResult.getMatchedRuleName() : "No Match")
                    .matchedRuleId(engineResult != null ? engineResult.getMatchedRuleId() : null)
                    .matchedRule(engineResult != null ? engineResult.getMatchedRule() : null)
                    .build();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid context type configured: " + config.getContextType());
        } catch (Exception e) {
            log.error("Evaluation failed", e);
            throw new RuntimeException("Evaluation failed: " + e.getMessage());
        }
    }
}
