package com.toystorage.backend.services.auth;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.Permission;
import com.toystorage.backend.repository.auth.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new BadRequest("PERMISSION_NOT_FOUND"));
    }

    public Permission getPermissionByCode(String code) {
        return permissionRepository.findByCode(code)
                .orElseThrow(() -> new BadRequest("PERMISSION_NOT_FOUND"));
    }

    public Permission createPermission(Permission permission) {
        if (permission == null) {
            throw new BadRequest("PERMISSION_REQUIRED");
        }

        if (permission.getCode() == null || permission.getCode().isBlank()) {
            throw new BadRequest("PERMISSION_CODE_REQUIRED");
        }

        if (permission.getName() == null || permission.getName().isBlank()) {
            throw new BadRequest("PERMISSION_NAME_REQUIRED");
        }

        String code = permission.getCode().trim().toUpperCase();

        if (permissionRepository.existsByCode(code)) {
            throw new BadRequest("PERMISSION_CODE_EXISTS");
        }

        permission.setCode(code);
        permission.setName(permission.getName().trim());

        if (permission.getDescription() != null) {
            permission.setDescription(permission.getDescription().trim());
        }

        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Long id, Permission permission) {
        Permission oldPermission = getPermissionById(id);

        if (permission == null) {
            throw new BadRequest("PERMISSION_REQUIRED");
        }

        if (permission.getCode() != null && !permission.getCode().isBlank()) {
            String code = permission.getCode().trim().toUpperCase();

            if (!code.equals(oldPermission.getCode()) && permissionRepository.existsByCode(code)) {
                throw new BadRequest("PERMISSION_CODE_EXISTS");
            }

            oldPermission.setCode(code);
        }

        if (permission.getName() != null && !permission.getName().isBlank()) {
            oldPermission.setName(permission.getName().trim());
        }

        if (permission.getDescription() != null) {
            oldPermission.setDescription(permission.getDescription().trim());
        }

        return permissionRepository.save(oldPermission);
    }

    public void deletePermission(Long id) {
        Permission permission = getPermissionById(id);
        permissionRepository.delete(permission);
    }
}