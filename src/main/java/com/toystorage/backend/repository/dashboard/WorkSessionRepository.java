package com.toystorage.backend.repository.dashboard;

import com.toystorage.backend.models.dashboard.WorkSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkSessionRepository
        extends JpaRepository<WorkSession, Long> {

    List<WorkSession> findByUserId(Long userId);

    List<WorkSession> findByReferenceTypeAndReferenceId(
            String referenceType,
            Long referenceId
    );
}