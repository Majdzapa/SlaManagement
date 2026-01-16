package com.company.sla.service.sla;

import com.company.sla.model.SlaConfiguration;
import com.company.sla.model.SlaRule;
import com.company.sla.model.context.ClientContext;
import com.company.sla.repository.SlaConfigurationRepository;
import com.company.sla.service.engine.RuleEngine;
import com.company.sla.dto.EvaluationResultDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendSwiftSLA implements SLA<ClientContext, Boolean> {

    private final SlaConfigurationRepository slaConfigurationRepository;
    private final RuleEngine ruleEngine;
    private static final String SLA_NAME = "SendSwiftSLA";

    public SendSwiftSLA(SlaConfigurationRepository slaConfigurationRepository, RuleEngine ruleEngine) {
        this.slaConfigurationRepository = slaConfigurationRepository;
        this.ruleEngine = ruleEngine;
    }

    @Override
    public Boolean evaluate(ClientContext context) {
         SlaConfiguration config = slaConfigurationRepository.findBySlaName(SLA_NAME)
                .orElseThrow(() -> new RuntimeException("SLA Configuration not found: " + SLA_NAME));

        if (!config.isActive()) {
            return false;
        }

        EvaluationResultDto engineResult = ruleEngine.determineResult(config, context);
        String result = (engineResult != null) ? engineResult.getResult() : null;
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
