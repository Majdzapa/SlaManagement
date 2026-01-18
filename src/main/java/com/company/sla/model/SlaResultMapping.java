package com.company.sla.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

@Getter
@Setter
@ToString(exclude = "slaConfiguration")
@EqualsAndHashCode(exclude = "slaConfiguration")
@NoArgsConstructor
@Entity
@Table(name = "sla_result_mapping")
public class SlaResultMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_id", nullable = false)
    @JsonIgnore
    private SlaConfiguration slaConfiguration;

    private BigDecimal minWeight;
    private BigDecimal maxWeight;
    private String resultValue;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SlaConfiguration getSlaConfiguration() {
        return slaConfiguration;
    }

    public void setSlaConfiguration(SlaConfiguration slaConfiguration) {
        this.slaConfiguration = slaConfiguration;
    }

    public BigDecimal getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(BigDecimal minWeight) {
        this.minWeight = minWeight;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
