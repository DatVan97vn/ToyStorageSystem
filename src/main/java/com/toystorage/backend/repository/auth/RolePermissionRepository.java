package com.toystorage.backend.repository.auth;

import com.toystorage.backend.models.auth.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository
        extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole_Id(Long roleId);

    void deleteByRole_Id(Long roleId);
    boolean existsByRole_IdAndPermission_Id(Long roleId, Long permissionId);

    void deleteByRole_IdAndPermission_Id(Long roleId, Long permissionId);
}