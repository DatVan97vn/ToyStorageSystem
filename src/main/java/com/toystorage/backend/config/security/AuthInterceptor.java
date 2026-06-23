package com.toystorage.backend.config.security;

import com.toystorage.backend.exceptions.BadRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        String uri = request.getRequestURI();

        if (uri.startsWith("/api/auth/login")
                || uri.startsWith("/api/auth/register")
                || uri.startsWith("/api/auth/register-staff")
                || uri.startsWith("/api/auth/change-password")
                || uri.startsWith("/api/2fa/setup")
                || uri.startsWith("/api/2fa/verify")
                || uri.startsWith("/api/2fa/login")) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new BadRequest("NOT_LOGIN");
        }

        Boolean authenticated = (Boolean) session.getAttribute("authenticated");

        if (!Boolean.TRUE.equals(authenticated)) {
            throw new BadRequest("NOT_AUTHENTICATED");
        }

        return true;
    }
}