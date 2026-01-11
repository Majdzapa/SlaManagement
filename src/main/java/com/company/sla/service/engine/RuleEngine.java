package com.company.sla.service.engine;

import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaResultMapping;
import com.company.sla.model.SlaRule;
import com.company.sla.repository.SlaResultMappingRepository;
import com.company.sla.repository.SlaRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleEngine {

    private final SlaRuleRepository ruleRepository;
    private final SlaResultMappingRepository resultMappingRepository;

    public BigDecimal calculateTotalWeight(SlaConfiguration slaConfig, Object context) {
        List<SlaRule> rules = ruleRepository.findBySlaConfigurationIdOrderByRuleOrderAsc(slaConfig.getId());
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (SlaRule rule : rules) {
            if (evaluateRule(rule, context)) {
                totalWeight = totalWeight.add(rule.getWeight());
            }
        }
        return totalWeight;
    }

    public String determineResult(SlaConfiguration slaConfig, BigDecimal totalWeight) {
        List<SlaResultMapping> mappings = resultMappingRepository.findBySlaConfigurationId(slaConfig.getId());
        
        for (SlaResultMapping mapping : mappings) {
            // Check if weight is within range [min, max]
            if (totalWeight.compareTo(mapping.getMinWeight()) >= 0 && 
                totalWeight.compareTo(mapping.getMaxWeight()) <= 0) {
                return mapping.getResultValue();
            }
        }
        return null; // Or default/error
    }

    private boolean evaluateRule(SlaRule rule, Object context) {
        try {
            Object actualValue = getFieldValue(context, rule.getConditionField());
            String expectedValue = rule.getConditionValue();
            String operator = rule.getConditionOperator();

            if (actualValue == null) return false;

            return compare(actualValue, expectedValue, operator);
        } catch (Exception e) {
            log.error("Error evaluating rule: {}", rule.getRuleName(), e);
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    private boolean compare(Object actual, String expected, String operator) {
        String actualStr = String.valueOf(actual);
        
        switch (operator.toUpperCase()) {
            case "EQUALS":
                return actualStr.equals(expected);
            case "NOT_EQUALS":
                return !actualStr.equals(expected);
            case "GREATER_THAN":
                try {
                    return new BigDecimal(actualStr).compareTo(new BigDecimal(expected)) > 0;
                } catch (NumberFormatException e) { return false; }
            case "LESS_THAN":
                try {
                    return new BigDecimal(actualStr).compareTo(new BigDecimal(expected)) < 0;
                } catch (NumberFormatException e) { return false; }
            case "CONTAINS":
                return actualStr.contains(expected);
            default:
                return false;
        }
    }
}
