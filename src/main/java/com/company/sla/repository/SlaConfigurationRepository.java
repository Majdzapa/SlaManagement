package com.company.sla.repository;

import com.company.sla.model.SlaConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlaConfigurationRepository extends JpaRepository<SlaConfiguration, Long> {
    Optional<SlaConfiguration> findBySlaName(String slaName);
    boolean existsBySlaName(String slaName);
}
