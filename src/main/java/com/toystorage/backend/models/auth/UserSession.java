package com.toystorage.backend.models.auth;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "session_token", unique = true)
    private String sessionToken;

    @Column(name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    private Boolean active;

    @PrePersist
    public void prePersist() {
        this.loginAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();

        if (this.expiredAt == null) {
            this.expiredAt = LocalDateTime.now().plusMinutes(15);
        }

        if (this.active == null) {
            this.active = true;
        }
    }
}