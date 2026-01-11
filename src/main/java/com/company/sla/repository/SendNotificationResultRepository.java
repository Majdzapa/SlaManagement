package com.company.sla.repository;

import com.company.sla.model.result.SendNotificationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendNotificationResultRepository extends JpaRepository<SendNotificationResult, Long> {
}
