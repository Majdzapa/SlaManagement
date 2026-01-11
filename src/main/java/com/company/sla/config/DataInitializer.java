package com.company.sla.config;

import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaResultMapping;
import com.company.sla.model.SlaRule;
import com.company.sla.repository.SlaConfigurationRepository;
import com.company.sla.repository.SlaResultMappingRepository;
import com.company.sla.repository.SlaRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final SlaConfigurationRepository slaRepository;
    private final SlaRuleRepository ruleRepository;
    private final SlaResultMappingRepository mappingRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (slaRepository.count() == 0) {
                // Create SendSwiftSLA
                SlaConfiguration sla = new SlaConfiguration();
                sla.setSlaName("SendSwiftSLA");
                sla.setSlaType("SendSwiftSLA");
                sla.setContextType("ClientContext");
                sla.setResultType("BOOLEAN");
                sla.setActive(true);
                sla = slaRepository.save(sla);

                // Rules
                createRule(sla, "USA Country", "country", "EQUALS", "USA", "0.3", 1);
                createRule(sla, "Premium Account", "accountType", "EQUALS", "PREMIUM", "0.4", 2);
                createRule(sla, "Low Risk", "riskScore", "LESS_THAN", "50", "0.2", 3);
                createRule(sla, "VIP Status", "isVIP", "EQUALS", "true", "0.1", 4);

                // Mappings
                createMapping(sla, "0.0", "0.4", "false", "Deny Transaction");
                createMapping(sla, "0.5", "1.0", "true", "Approve Transaction");
                
                System.out.println("Initialized SendSwiftSLA Configuration");
            }
        };
    }

    private void createRule(SlaConfiguration sla, String name, String field, String operator, String value, String weight, int order) {
        SlaRule rule = new SlaRule();
        rule.setSlaConfiguration(sla);
        rule.setRuleName(name);
        rule.setConditionField(field);
        rule.setConditionOperator(operator);
        rule.setConditionValue(value);
        rule.setWeight(new BigDecimal(weight));
        rule.setRuleOrder(order);
        rule.setActive(true);
        ruleRepository.save(rule);
    }

    private void createMapping(SlaConfiguration sla, String min, String max, String result, String desc) {
        SlaResultMapping mapping = new SlaResultMapping();
        mapping.setSlaConfiguration(sla);
        mapping.setMinWeight(new BigDecimal(min));
        mapping.setMaxWeight(new BigDecimal(max));
        mapping.setResultValue(result);
        mapping.setDescription(desc);
        mappingRepository.save(mapping);
    }
}
