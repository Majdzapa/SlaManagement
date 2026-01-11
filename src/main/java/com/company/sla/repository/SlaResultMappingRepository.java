package com.company.sla.repository;

import com.company.sla.model.SlaResultMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlaResultMappingRepository extends JpaRepository<SlaResultMapping, Long> {
    List<SlaResultMapping> findBySlaConfigurationId(Long slaId);
}
