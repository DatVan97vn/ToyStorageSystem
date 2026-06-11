package com.toystorage.backend.mapper.auth;

import com.toystorage.backend.dto.response.auth.UserResponse;
import com.toystorage.backend.models.auth.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setStatus(user.getStatus());
        response.setFirstLogin(user.getFirstLogin());
        response.setTwoFactorEnabled(user.getTwoFactorEnabled());

        return response;
    }
}