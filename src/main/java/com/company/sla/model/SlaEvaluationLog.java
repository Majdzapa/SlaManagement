package com.company.sla.model;

import jakarta.persistence.*;
import lombok.Data; // Keeping for reference but unused for methods now
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sla_evaluation_log")
@EntityListeners(AuditingEntityListener.class)
public class SlaEvaluationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_id", nullable = false)
    private SlaConfiguration slaConfiguration;

    @Column(columnDefinition = "TEXT")
    private String contextJson;

    private BigDecimal totalWeight;
    private String resultValue;

    @CreatedDate
    private LocalDateTime evaluationTimestamp;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SlaConfiguration getSlaConfiguration() { return slaConfiguration; }
    public void setSlaConfiguration(SlaConfiguration slaConfiguration) { this.slaConfiguration = slaConfiguration; }
    public String getContextJson() { return contextJson; }
    public void setContextJson(String contextJson) { this.contextJson = contextJson; }
    public BigDecimal getTotalWeight() { return totalWeight; }
    public void setTotalWeight(BigDecimal totalWeight) { this.totalWeight = totalWeight; }
    public String getResultValue() { return resultValue; }
    public void setResultValue(String resultValue) { this.resultValue = resultValue; }
    public LocalDateTime getEvaluationTimestamp() { return evaluationTimestamp; }
    public void setEvaluationTimestamp(LocalDateTime evaluationTimestamp) { this.evaluationTimestamp = evaluationTimestamp; }
}
