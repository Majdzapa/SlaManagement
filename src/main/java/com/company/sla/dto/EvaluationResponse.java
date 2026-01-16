package com.company.sla.dto;

import java.math.BigDecimal;

public class EvaluationResponse {
    private boolean result;
    private String resultValue;
    private BigDecimal totalWeight;
    private String slaName;
    private String matchedRuleName;
    private Long matchedRuleId;
    private MatchedRuleDto matchedRule;

    public EvaluationResponse() {}

    public EvaluationResponse(boolean result, String resultValue, BigDecimal totalWeight, String slaName, String matchedRuleName, Long matchedRuleId, MatchedRuleDto matchedRule) {
        this.result = result;
        this.resultValue = resultValue;
        this.totalWeight = totalWeight;
        this.slaName = slaName;
        this.matchedRuleName = matchedRuleName;
        this.matchedRuleId = matchedRuleId;
        this.matchedRule = matchedRule;
    }

    public boolean isResult() { return result; }
    public void setResult(boolean result) { this.result = result; }

    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }

    public BigDecimal getTotalWeight() { return totalWeight; }
    public void setTotalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; }

    public String getSlaName() { return slaName; }
    public void setSlaName(String slaName) { this.slaName = slaName; }

    public String getMatchedRuleName() { return matchedRuleName; }
    public void setMatchedRuleName(String matchedRuleName) { this.matchedRuleName = matchedRuleName; }

    public Long getMatchedRuleId() { return matchedRuleId; }
    public void setMatchedRuleId(Long matchedRuleId) { this.matchedRuleId = matchedRuleId; }

    public MatchedRuleDto getMatchedRule() { return matchedRule; }
    public void setMatchedRule(MatchedRuleDto matchedRule) { this.matchedRule = matchedRule; }
    
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private boolean result;
        private String resultValue;
        private BigDecimal totalWeight;
        private String slaName;
        private String matchedRuleName;
        private Long matchedRuleId;
        private MatchedRuleDto matchedRule;

        public Builder result(boolean result) { this.result = result; return this; }
        public Builder resultValue(String resultValue) { this.resultValue = resultValue; return this; }
        public Builder totalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; return this; }
        public Builder slaName(String slaName) { this.slaName = slaName; return this; }
        public Builder matchedRuleName(String matchedRuleName) { this.matchedRuleName = matchedRuleName; return this; }
        public Builder matchedRuleId(Long matchedRuleId) { this.matchedRuleId = matchedRuleId; return this; }
        public Builder matchedRule(MatchedRuleDto matchedRule) { this.matchedRule = matchedRule; return this; }
        
        public EvaluationResponse build() {
            return new EvaluationResponse(result, resultValue, totalWeight, slaName, matchedRuleName, matchedRuleId, matchedRule);
        }
    }
}
