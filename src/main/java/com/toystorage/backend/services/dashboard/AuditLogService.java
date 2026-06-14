package com.toystorage.backend.services.dashboard;

import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.dashboard.AuditLog;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.dashboard.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLog log(
            Long userId,
            String action,
            String entityName,
            Integer entityId,
            String oldData,
            String newData
    ) {
        User user = null;

        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .oldData(oldData)
                .newData(newData)
                .build();

        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByUser(Long userId) {
        return auditLogRepository.findByUser_Id(userId);
    }

    public List<AuditLog> getLogsByEntityName(String entityName) {
        return auditLogRepository.findByEntityName(entityName);
    }
}