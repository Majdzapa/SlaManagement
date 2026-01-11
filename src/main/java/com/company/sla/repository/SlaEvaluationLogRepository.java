package com.company.sla.repository;

import com.company.sla.model.SlaEvaluationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlaEvaluationLogRepository extends JpaRepository<SlaEvaluationLog, Long> {
}
