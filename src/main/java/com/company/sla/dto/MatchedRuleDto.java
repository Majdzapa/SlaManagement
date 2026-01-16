package com.company.sla.dto;

public class MatchedRuleDto {
    private Long id;
    private String ruleName;
    private String conditionsJson;
    private Long resultInstanceId;
    private String resultValue;
    private Integer ruleOrder;
    private boolean active;

    public MatchedRuleDto() {}

    public MatchedRuleDto(Long id, String ruleName, String conditionsJson, Long resultInstanceId, String resultValue, Integer ruleOrder, boolean active) {
        this.id = id;
        this.ruleName = ruleName;
        this.conditionsJson = conditionsJson;
        this.resultInstanceId = resultInstanceId;
        this.resultValue = resultValue;
        this.ruleOrder = ruleOrder;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public String getConditionsJson() { return conditionsJson; }
    public void setConditionsJson(String conditionsJson) { this.conditionsJson = conditionsJson; }
    public Long getResultInstanceId() { return resultInstanceId; }
    public void setResultInstanceId(Long resultInstanceId) { this.resultInstanceId = resultInstanceId; }
    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }
    public Integer getRuleOrder() { return ruleOrder; }
    public void setRuleOrder(Integer ruleOrder) { this.ruleOrder = ruleOrder; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static MatchedRuleDto fromSlaRule(com.company.sla.model.SlaRule rule) {
        if (rule == null) return null;
        return new MatchedRuleDto(
            rule.getId(),
            rule.getRuleName(),
            rule.getConditionsJson(),
            rule.getResultInstanceId(),
            rule.getResultValue(),
            rule.getRuleOrder(),
            rule.isActive()
        );
    }
}
