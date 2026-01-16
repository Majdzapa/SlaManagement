package com.company.sla.service.engine;

import com.company.sla.dto.ContextDto.ContextClassInfo;
import com.company.sla.dto.ContextDto.ContextFieldInfo;
import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaRule;
import com.company.sla.repository.SlaRuleRepository;
import com.company.sla.service.SlaContextService;
import com.company.sla.dto.EvaluationResultDto;
import com.company.sla.dto.MatchedRuleDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RuleEngine {

    private static final Logger log = LoggerFactory.getLogger(RuleEngine.class);

    private final SlaRuleRepository slaRuleRepository;
    private final SlaContextService slaContextService;
    private final ObjectMapper objectMapper;

    public RuleEngine(SlaRuleRepository slaRuleRepository,
                      SlaContextService slaContextService,
                      ObjectMapper objectMapper) {
        this.slaRuleRepository = slaRuleRepository;
        this.slaContextService = slaContextService;
        this.objectMapper = objectMapper;
    }

    /**
     * Evaluates the rules for a given SLA Configuration and Context object.
     * Returns the Result Instance ID (as String) of the Best Match rule.
     */
    public EvaluationResultDto determineResult(SlaConfiguration config, Object contextObj) {
        List<SlaRule> rules = slaRuleRepository.findBySlaConfigurationIdAndIsActiveTrueOrderByRuleOrderAsc(config.getId());
        
        // 1. Get Context Metadata to retrieve Field Weights
        Optional<ContextClassInfo> contextInfoOpt = slaContextService.getAvailableContexts().stream()
                .filter(c -> c.getClassName().equals(config.getContextType()))
                .findFirst();

        if (contextInfoOpt.isEmpty()) {
            throw new RuntimeException("Context type info not found for: " + config.getContextType());
        }
        ContextClassInfo contextInfo = contextInfoOpt.get();

        SlaRule bestMatchRule = null;
        double maxScore = -1.0;

        // Convert context object to Map for easier comparison
        Map<String, Object> contextMap = objectMapper.convertValue(contextObj, new TypeReference<Map<String, Object>>() {});

        // 2. Iterate Rules (Rows)
        for (SlaRule rule : rules) {
            try {
                Map<String, Object> conditions = objectMapper.readValue(rule.getConditionsJson(), new TypeReference<Map<String, Object>>() {});
                
                double currentScore = calculateMatchScore(conditions, contextMap, contextInfo);
                
                log.info("Rule '{}' conditions: {} | Match Score: {}", 
                    rule.getRuleName(), conditions, currentScore);
                
                // If it's a match (> -1) and score is higher than max
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    bestMatchRule = rule;
                }

            } catch (Exception e) {
                // Log error parsing rule?
                continue;
            }
        }

        if (bestMatchRule != null) {
            log.info("Best Match Rule Found: '{}' with Score: {}", bestMatchRule.getRuleName(), maxScore);
            
            String resultVal;
            // Smart Result Selection: Use resultValue for primitives, resultInstanceId for entities
            if (isPrimitiveType(config.getResultType())) {
                resultVal = bestMatchRule.getResultValue();
            } else if (bestMatchRule.getResultInstanceId() != null) {
                resultVal = String.valueOf(bestMatchRule.getResultInstanceId());
            } else {
                resultVal = bestMatchRule.getResultValue();
            }

            return EvaluationResultDto.builder()
                .result(resultVal)
                .matchedRuleName(bestMatchRule.getRuleName())
                .matchedRuleId(bestMatchRule.getId())
                .score(maxScore)
                .matchedRule(MatchedRuleDto.fromSlaRule(bestMatchRule))
                .build();
        }

        log.warn("No matching rule found for SLA: {}", config.getSlaName());
        return null; // Or default result
    }

    private boolean isPrimitiveType(String resultType) {
        if (resultType == null) return false;
        String upper = resultType.toUpperCase();
        return upper.equals("BOOLEAN") || upper.equals("STRING") || upper.equals("INTEGER");
    }

    private double calculateMatchScore(Map<String, Object> conditions, Map<String, Object> contextValue, ContextClassInfo contextInfo) {
        double score = 0.0;
        
        for (Map.Entry<String, Object> condition : conditions.entrySet()) {
            String fieldName = condition.getKey();
            Object requiredValue = condition.getValue();
            Object actualValue = contextValue.get(fieldName);

            // If actualValue is missing in input, we don't reject the rule (Lenient)
            // We just don't add to the score.
            if (actualValue == null) {
                continue;
            }

            // If it is present, it MUST match
            if (!valuesMatch(requiredValue, actualValue)) {
                return -1.0;
            }

            // If match, add weight
            Double weight = getWeightForField(fieldName, contextInfo);
            score += weight;
        }
        return score;
    }

    private boolean valuesMatch(Object required, Object actual) {
        if (required == null) return actual == null;
        return required.toString().equals(actual != null ? actual.toString() : null);
    }

    private Double getWeightForField(String fieldName, ContextClassInfo info) {
        return info.getFields().stream()
                .filter(f -> f.getFieldName().equals(fieldName))
                .map(ContextFieldInfo::getMetricWeight)
                .findFirst()
                .orElse(0.0);
    }

    // Deprecated methods from old engine, kept empty or removed
    public BigDecimal calculateTotalWeight(SlaConfiguration config, Object context) { return BigDecimal.ZERO; }
}
