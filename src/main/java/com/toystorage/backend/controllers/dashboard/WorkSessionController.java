package com.toystorage.backend.controllers.dashboard;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.dashboard.WorkSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/work-sessions")
@RequiredArgsConstructor

public class WorkSessionController {

    private final WorkSessionService workSessionService;

    @PostMapping("/start")
    public ResponseEntity<?> startSession(
            @RequestBody Map<String, Object> payload
    ) {
        if (payload == null) {
            throw new BadRequest("PAYLOAD_REQUIRED");
        }

        Long userId = Long.valueOf(payload.get("userId").toString());
        String referenceType = payload.get("referenceType").toString();
        Long referenceId = Long.valueOf(payload.get("referenceId").toString());

        return ResponseEntity.ok(
                workSessionService.startSession(
                        userId,
                        referenceType,
                        referenceId
                )
        );
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<?> endSession(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                workSessionService.endSession(id)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllSessions() {
        return ResponseEntity.ok(
                workSessionService.getAllSessions()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getSessionsByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                workSessionService.getSessionsByUser(userId)
        );
    }

    @GetMapping("/reference/{referenceType}/{referenceId}")
    public ResponseEntity<?> getSessionsByReference(
            @PathVariable String referenceType,
            @PathVariable Long referenceId
    ) {
        return ResponseEntity.ok(
                workSessionService.getSessionsByReference(
                        referenceType,
                        referenceId
                )
        );
    }
}