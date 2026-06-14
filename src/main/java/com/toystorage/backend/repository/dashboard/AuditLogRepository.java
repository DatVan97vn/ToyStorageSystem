package com.toystorage.backend.repository.dashboard;

import com.toystorage.backend.models.dashboard.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUser_Id(Long userId);

    List<AuditLog> findByEntityName(String entityName);
}