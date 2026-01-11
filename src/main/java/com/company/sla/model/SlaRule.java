package com.company.sla.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String conditionField;
    private String conditionOperator; // EQUALS, GREATER_THAN, etc.
    private String conditionValue;
    private BigDecimal weight;
    private Integer ruleOrder;
    private boolean isActive = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SlaConfiguration getSlaConfiguration() { return slaConfiguration; }
    public void setSlaConfiguration(SlaConfiguration slaConfiguration) { this.slaConfiguration = slaConfiguration; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getConditionField() { return conditionField; }
    public void setConditionField(String conditionField) { this.conditionField = conditionField; }

    public String getConditionOperator() { return conditionOperator; }
    public void setConditionOperator(String conditionOperator) { this.conditionOperator = conditionOperator; }

    public String getConditionValue() { return conditionValue; }
    public void setConditionValue(String conditionValue) { this.conditionValue = conditionValue; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public Integer getRuleOrder() { return ruleOrder; }
    public void setRuleOrder(Integer ruleOrder) { this.ruleOrder = ruleOrder; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
}
