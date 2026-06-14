package com.toystorage.backend.services.dashboard;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.dashboard.WorkSession;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.dashboard.WorkSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkSessionService {

    private final WorkSessionRepository workSessionRepository;
    private final UserRepository userRepository;

    public WorkSession startSession(
            Long userId,
            String referenceType,
            Long referenceId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequest("USER_NOT_FOUND"));

        WorkSession session = WorkSession.builder()
                .user(user)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .startedAt(LocalDateTime.now())
                .build();

        return workSessionRepository.save(session);
    }

    public WorkSession endSession(Long sessionId) {
        WorkSession session = workSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BadRequest("WORK_SESSION_NOT_FOUND"));

        if (session.getEndedAt() != null) {
            throw new BadRequest("WORK_SESSION_ALREADY_ENDED");
        }

        LocalDateTime endedAt = LocalDateTime.now();

        session.setEndedAt(endedAt);
        session.setDuration(
                (int) Duration.between(
                        session.getStartedAt(),
                        endedAt
                ).toMinutes()
        );

        return workSessionRepository.save(session);
    }

    public List<WorkSession> getSessionsByUser(Long userId) {
        return workSessionRepository.findByUserId(userId);
    }

    public List<WorkSession> getSessionsByReference(
            String referenceType,
            Long referenceId
    ) {
        return workSessionRepository.findByReferenceTypeAndReferenceId(
                referenceType,
                referenceId
        );
    }

    public List<WorkSession> getAllSessions() {
        return workSessionRepository.findAll();
    }
}