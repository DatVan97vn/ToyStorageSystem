package com.toystorage.backend.services.auth;

import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.auth.UserSession;
import com.toystorage.backend.repository.auth.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    public UserSession createSession(User user) {
        UserSession session = UserSession.builder()
                .user(user)
                .sessionToken(UUID.randomUUID().toString())
                .loginAt(LocalDateTime.now())
                .lastActivityAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .active(true)
                .build();

        return userSessionRepository.save(session);
    }

    public void updateActivity(String token) {
        UserSession session = userSessionRepository.findBySessionToken(token)
                .orElse(null);

        if (session == null || Boolean.FALSE.equals(session.getActive())) {
            return;
        }

        session.setLastActivityAt(LocalDateTime.now());
        session.setExpiredAt(LocalDateTime.now().plusMinutes(15));

        userSessionRepository.save(session);
    }

    public boolean isSessionValid(String token) {
        UserSession session = userSessionRepository.findBySessionToken(token)
                .orElse(null);

        if (session == null || Boolean.FALSE.equals(session.getActive())) {
            return false;
        }

        return session.getExpiredAt().isAfter(LocalDateTime.now());
    }

    public void logout(String token) {
        UserSession session = userSessionRepository.findBySessionToken(token)
                .orElse(null);

        if (session == null) {
            return;
        }

        session.setActive(false);
        userSessionRepository.save(session);
    }
}