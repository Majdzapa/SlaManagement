package com.company.sla.service.sla;

import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaRule;
import com.company.sla.model.context.ClientContext;
import com.company.sla.repository.SlaConfigurationRepository;
import com.company.sla.service.engine.RuleEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SendSwiftSLA implements SLA<ClientContext, Boolean> {

    private final SlaConfigurationRepository slaConfigurationRepository;
    private final RuleEngine ruleEngine;

    private static final String SLA_NAME = "SendSwiftSLA";

    @Override
    public Boolean evaluate(ClientContext context) {
        SlaConfiguration config = slaConfigurationRepository.findBySlaName(SLA_NAME)
                .orElseThrow(() -> new RuntimeException("SLA Configuration not found: " + SLA_NAME));

        if (!config.isActive()) {
            return false; // Default to false if inactive
        }

        BigDecimal totalWeight = ruleEngine.calculateTotalWeight(config, context);
        String result = ruleEngine.determineResult(config, totalWeight);

        return Boolean.parseBoolean(result);
    }

    @Override
    public String getSlaType() {
        return SLA_NAME;
    }

    @Override
    public List<SlaRule> getRules() {
        return slaConfigurationRepository.findBySlaName(SLA_NAME)
                .map(SlaConfiguration::getRules)
                .orElse(List.of());
    }
}
