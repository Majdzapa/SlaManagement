package com.company.sla.service;

import com.company.sla.dto.EvaluationRequest;
import com.company.sla.dto.EvaluationResponse;
import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaEvaluationLog;
import com.company.sla.repository.SlaConfigurationRepository;
import com.company.sla.repository.SlaEvaluationLogRepository;
import com.company.sla.service.engine.RuleEngine;
import com.company.sla.service.sla.SLA;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlaEvaluationService {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SlaEvaluationService.class);

    private final SlaConfigurationRepository slaConfigurationRepository;
    private final SlaEvaluationLogRepository evaluationLogRepository;
    private final RuleEngine ruleEngine;
    private final ObjectMapper objectMapper;
    private final List<SLA<?, ?>> slaImplementations;

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
            BigDecimal totalWeight = ruleEngine.calculateTotalWeight(config, contextObj);
            String result = ruleEngine.determineResult(config, totalWeight);

            // 3. Log Evaluation
            SlaEvaluationLog logEntry = new SlaEvaluationLog();
            logEntry.setSlaConfiguration(config);
            logEntry.setContextJson(objectMapper.writeValueAsString(request.getContext()));
            logEntry.setTotalWeight(totalWeight);
            logEntry.setResultValue(result);
            evaluationLogRepository.save(logEntry);

            return EvaluationResponse.builder()
                    .result(Boolean.parseBoolean(result)) // Assuming boolean result mostly, or generic string
                    .resultValue(result)
                    .totalWeight(totalWeight)
                    .slaName(config.getSlaName())
                    .build();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid context type configured: " + config.getContextType());
        } catch (Exception e) {
            log.error("Evaluation failed", e);
            throw new RuntimeException("Evaluation failed: " + e.getMessage());
        }
    }
}
