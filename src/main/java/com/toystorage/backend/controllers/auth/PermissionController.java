package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.Permission;
import com.toystorage.backend.services.auth.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<?> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPermissionById(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequest("PERMISSION_ID_REQUIRED");
        }

        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getPermissionByCode(@PathVariable String code) {
        if (code == null || code.isBlank()) {
            throw new BadRequest("PERMISSION_CODE_REQUIRED");
        }

        return ResponseEntity.ok(permissionService.getPermissionByCode(code));
    }

    @PostMapping
    public ResponseEntity<?> createPermission(
            @RequestBody Permission permission
    ) {
        return ResponseEntity.ok(
                permissionService.createPermission(permission)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePermission(
            @PathVariable Long id,
            @RequestBody Permission permission
    ) {
        if (id == null) {
            throw new BadRequest("PERMISSION_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                permissionService.updatePermission(id, permission)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequest("PERMISSION_ID_REQUIRED");
        }

        permissionService.deletePermission(id);

        return ResponseEntity.ok("DELETE_PERMISSION_SUCCESS");
    }
}