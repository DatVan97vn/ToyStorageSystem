package com.toystorage.backend.mapper.auth;

import com.toystorage.backend.dto.request.auth.RegisterRequest;
import com.toystorage.backend.dto.request.auth.RegisterStaffRequest;
import com.toystorage.backend.enums.users.UserStatus;
import com.toystorage.backend.models.auth.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toUser(RegisterRequest request, String encodedPassword) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().trim());
        user.setPassword(encodedPassword);
        user.setStatus(UserStatus.ACTIVE);
        user.setFirstLogin(false);
        user.setTwoFactorEnabled(false);
        return user;
    }

    public User toStaff(RegisterStaffRequest request, String encodedPassword) {
        User staff = new User();
        staff.setName(request.getName());
        staff.setEmail(request.getEmail().trim());
        staff.setPassword(encodedPassword);
        staff.setStatus(UserStatus.ACTIVE);
        staff.setFirstLogin(true);
        staff.setTwoFactorEnabled(false);
        return staff;
    }
}