package com.company.sla.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data; // Unused for methods

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "sla_rule")
public class SlaRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_id", nullable = false)
    @JsonIgnore
    private SlaConfiguration slaConfiguration;

    private String ruleName;

    // A collection of conditions: {"country": "USA", "accountType": "PREMIUM"}
    @Column(columnDefinition = "TEXT")
    private String conditionsJson;

    // The result to return if this rule is the best match
    private Long resultInstanceId;
    
    // Fallback for primitive results or explicit values
    private String resultValue;

    // Legacy support or if we still want to order rules manually? 
    // Best Match logic implies score defines winner, but order can break ties.
    private Integer ruleOrder;
    
    private boolean isActive = true;

    // Manual Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public SlaConfiguration getSlaConfiguration() { return slaConfiguration; }
    public void setSlaConfiguration(SlaConfiguration slaConfiguration) { this.slaConfiguration = slaConfiguration; }
    public String getConditionsJson() { return conditionsJson; }
    public void setConditionsJson(String conditionsJson) { this.conditionsJson = conditionsJson; }
    public Long getResultInstanceId() { return resultInstanceId; }
    public void setResultInstanceId(Long resultInstanceId) { this.resultInstanceId = resultInstanceId; }
    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }
    public Integer getRuleOrder() { return ruleOrder; }
    public void setRuleOrder(Integer ruleOrder) { this.ruleOrder = ruleOrder; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
