package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.auth.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor

public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping("/{roleId}/permissions")
    public ResponseEntity<?> getPermissionsByRole(
            @PathVariable Long roleId
    ) {
        if (roleId == null) {
            throw new BadRequest("ROLE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                rolePermissionService.getPermissionsByRole(roleId)
        );
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<?> addPermissionToRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId
    ) {
        return ResponseEntity.ok(
                rolePermissionService.addPermissionToRole(roleId, permissionId)
        );
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<?> removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId
    ) {
        rolePermissionService.removePermissionFromRole(roleId, permissionId);

        return ResponseEntity.ok("REMOVE_PERMISSION_FROM_ROLE_SUCCESS");
    }
}