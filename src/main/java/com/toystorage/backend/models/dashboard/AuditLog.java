package com.toystorage.backend.models.dashboard;

import com.toystorage.backend.models.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String action;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "old_data", columnDefinition = "TEXT")
    private String oldData;

    @Column(name = "new_data", columnDefinition = "TEXT")
    private String newData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
