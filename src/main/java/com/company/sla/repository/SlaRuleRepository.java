package com.company.sla.repository;

import com.company.sla.model.SlaRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlaRuleRepository extends JpaRepository<SlaRule, Long> {
    List<SlaRule> findBySlaConfigurationIdOrderByRuleOrderAsc(Long slaId);
    List<SlaRule> findBySlaConfigurationIdAndIsActiveTrueOrderByRuleOrderAsc(Long slaConfigurationId);
}
