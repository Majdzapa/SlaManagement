package com.company.sla.config;

import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaResultMapping;
import com.company.sla.model.SlaRule;
import com.company.sla.repository.SlaConfigurationRepository;
import com.company.sla.repository.SlaResultMappingRepository;
import com.company.sla.repository.SlaRuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    private final SlaConfigurationRepository slaRepository;
    private final SlaRuleRepository ruleRepository;
    private final SlaResultMappingRepository mappingRepository;

    public DataInitializer(SlaConfigurationRepository slaRepository,
                           SlaRuleRepository ruleRepository,
                           SlaResultMappingRepository mappingRepository) {
        this.slaRepository = slaRepository;
        this.ruleRepository = ruleRepository;
        this.mappingRepository = mappingRepository;
    }

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
                // Implicit weights from ClientContext:
                // clientId (0.1), country (0.2), accountType (0.3), riskScore (0.4), isVIP (0.5), amount (0.6)
                
                // Rule 1: Country = USA (Score: 0.2)
                createRule(sla, "USA Country", "{\"country\": \"USA\"}", null, 1);
                
                // Rule 2: Premium Account (Score: 0.3)
                createRule(sla, "Premium Account", "{\"accountType\": \"PREMIUM\"}", null, 2);
                
                // Rule 3: USA + Premium (Score: 0.2 + 0.3 = 0.5)
                createRule(sla, "USA Premium", "{\"country\": \"USA\", \"accountType\": \"PREMIUM\"}", null, 3);
            
                // Mappings (Legacy support, might not be used by new engine but kept for table integrity)
                createMapping(sla, "0.0", "0.4", "false", "Deny Transaction");
                createMapping(sla, "0.5", "1.0", "true", "Approve Transaction");
                
                System.out.println("Initialized SendSwiftSLA Configuration");
            }
        };
    }

    private void createRule(SlaConfiguration sla, String name, String jsonConditions, Long resultInstanceId, int order) {
        SlaRule rule = new SlaRule();
        rule.setSlaConfiguration(sla);
        rule.setRuleName(name);
        rule.setConditionsJson(jsonConditions);
        rule.setResultInstanceId(resultInstanceId);
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
