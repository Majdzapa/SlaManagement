package com.company.sla.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(exclude = { "rules", "resultMappings" })
@EqualsAndHashCode(exclude = { "rules", "resultMappings" })
@NoArgsConstructor
@Entity
@Table(name = "sla_configuration")
@EntityListeners(AuditingEntityListener.class)
public class SlaConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slaName;

    @Column(nullable = false)
    private String slaType; // e.g., "SendSwiftSLA"

    @Column(nullable = false)
    private String contextType; // e.g., "Client"

    @Column(nullable = false)
    private String resultType; // e.g., "BOOLEAN"

    private boolean isActive = true;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "slaConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SlaRule> rules;

    @OneToMany(mappedBy = "slaConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SlaResultMapping> resultMappings;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlaName() {
        return slaName;
    }

    public void setSlaName(String slaName) {
        this.slaName = slaName;
    }

    public String getSlaType() {
        return slaType;
    }

    public void setSlaType(String slaType) {
        this.slaType = slaType;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<SlaRule> getRules() {
        return rules;
    }

    public void setRules(List<SlaRule> rules) {
        this.rules = rules;
    }

    public List<SlaResultMapping> getResultMappings() {
        return resultMappings;
    }

    public void setResultMappings(List<SlaResultMapping> resultMappings) {
        this.resultMappings = resultMappings;
    }
}
