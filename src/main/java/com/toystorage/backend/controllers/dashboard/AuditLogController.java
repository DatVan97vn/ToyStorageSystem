package com.toystorage.backend.controllers.dashboard;

import com.toystorage.backend.services.dashboard.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<?> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(auditLogService.getLogsByUser(userId));
    }

    @GetMapping("/entity/{entityName}")
    public ResponseEntity<?> getLogsByEntityName(
            @PathVariable String entityName
    ) {
        return ResponseEntity.ok(
                auditLogService.getLogsByEntityName(entityName)
        );
    }
}