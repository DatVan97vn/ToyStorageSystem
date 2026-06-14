package com.toystorage.backend.services.auth;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.Permission;
import com.toystorage.backend.models.auth.Role;
import com.toystorage.backend.models.auth.RolePermission;
import com.toystorage.backend.repository.auth.PermissionRepository;
import com.toystorage.backend.repository.auth.RolePermissionRepository;
import com.toystorage.backend.repository.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public List<RolePermission> getPermissionsByRole(Long roleId) {
        if (roleId == null) {
            throw new BadRequest("ROLE_ID_REQUIRED");
        }

        return rolePermissionRepository.findByRole_Id(roleId);
    }

    public RolePermission addPermissionToRole(
            Long roleId,
            Long permissionId
    ) {
        if (roleId == null) {
            throw new BadRequest("ROLE_ID_REQUIRED");
        }

        if (permissionId == null) {
            throw new BadRequest("PERMISSION_ID_REQUIRED");
        }

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BadRequest("ROLE_NOT_FOUND"));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new BadRequest("PERMISSION_NOT_FOUND"));

        if (rolePermissionRepository.existsByRole_IdAndPermission_Id(roleId, permissionId)) {
            throw new BadRequest("ROLE_PERMISSION_EXISTS");
        }

        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        return rolePermissionRepository.save(rolePermission);
    }

    @Transactional
    public void removePermissionFromRole(
            Long roleId,
            Long permissionId
    ) {
        if (roleId == null) {
            throw new BadRequest("ROLE_ID_REQUIRED");
        }

        if (permissionId == null) {
            throw new BadRequest("PERMISSION_ID_REQUIRED");
        }

        if (!rolePermissionRepository.existsByRole_IdAndPermission_Id(roleId, permissionId)) {
            throw new BadRequest("ROLE_PERMISSION_NOT_FOUND");
        }

        rolePermissionRepository.deleteByRole_IdAndPermission_Id(roleId, permissionId);
    }
}