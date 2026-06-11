package com.toystorage.backend.repository.dashboard;

import com.toystorage.backend.models.dashboard.WorkSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkSessionRepository
        extends JpaRepository<WorkSession, Long> {
}