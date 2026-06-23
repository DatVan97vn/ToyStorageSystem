package com.toystorage.backend.controllers.auth;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.Role;
import com.toystorage.backend.services.auth.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor

public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequest("ROLE_ID_REQUIRED");
        }

        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getRoleByCode(@PathVariable String code) {
        if (code == null || code.isBlank()) {
            throw new BadRequest("ROLE_CODE_REQUIRED");
        }

        return ResponseEntity.ok(roleService.getRoleByCode(code));
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody Role role
    ) {
        if (id == null) {
            throw new BadRequest("ROLE_ID_REQUIRED");
        }

        return ResponseEntity.ok(roleService.updateRole(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        if (id == null) {
            throw new BadRequest("ROLE_ID_REQUIRED");
        }

        roleService.deleteRole(id);

        return ResponseEntity.ok("DELETE_ROLE_SUCCESS");
    }
}