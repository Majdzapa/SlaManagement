package com.company.sla.dto;

public class EvaluationResultDto {
    private String result;
    private String matchedRuleName;
    private Long matchedRuleId;
    private double score;
    private MatchedRuleDto matchedRule;

    public EvaluationResultDto() {}

    public EvaluationResultDto(String result, String matchedRuleName, Long matchedRuleId, double score, MatchedRuleDto matchedRule) {
        this.result = result;
        this.matchedRuleName = matchedRuleName;
        this.matchedRuleId = matchedRuleId;
        this.score = score;
        this.matchedRule = matchedRule;
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getMatchedRuleName() { return matchedRuleName; }
    public void setMatchedRuleName(String matchedRuleName) { this.matchedRuleName = matchedRuleName; }

    public Long getMatchedRuleId() { return matchedRuleId; }
    public void setMatchedRuleId(Long matchedRuleId) { this.matchedRuleId = matchedRuleId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public MatchedRuleDto getMatchedRule() { return matchedRule; }
    public void setMatchedRule(MatchedRuleDto matchedRule) { this.matchedRule = matchedRule; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String result;
        private String matchedRuleName;
        private Long matchedRuleId;
        private double score;
        private MatchedRuleDto matchedRule;

        public Builder result(String result) { this.result = result; return this; }
        public Builder matchedRuleName(String matchedRuleName) { this.matchedRuleName = matchedRuleName; return this; }
        public Builder matchedRuleId(Long matchedRuleId) { this.matchedRuleId = matchedRuleId; return this; }
        public Builder score(double score) { this.score = score; return this; }
        public Builder matchedRule(MatchedRuleDto matchedRule) { this.matchedRule = matchedRule; return this; }

        public EvaluationResultDto build() {
            return new EvaluationResultDto(result, matchedRuleName, matchedRuleId, score, matchedRule);
        }
    }
}
