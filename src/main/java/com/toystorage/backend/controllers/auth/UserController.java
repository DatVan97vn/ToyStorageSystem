package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.enums.users.UserStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.auth.UserMapper;
import com.toystorage.backend.services.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers()
                        .stream()
                        .map(userMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequest("USER_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                userMapper.toResponse(userService.getUserById(id))
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        if (id == null) {
            throw new BadRequest("USER_ID_REQUIRED");
        }

        String statusValue = request.get("status");

        if (statusValue == null || statusValue.isBlank()) {
            throw new BadRequest("USER_STATUS_REQUIRED");
        }

        UserStatus status;

        try {
            status = UserStatus.valueOf(statusValue.toUpperCase().trim());
        } catch (Exception e) {
            throw new BadRequest("INVALID_USER_STATUS");
        }

        return ResponseEntity.ok(
                userMapper.toResponse(
                        userService.updateUserStatus(id, status)
                )
        );
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequest("USER_ID_REQUIRED");
        }

        userService.resetPassword(id);

        return ResponseEntity.ok("RESET_PASSWORD_SUCCESS");
    }
}