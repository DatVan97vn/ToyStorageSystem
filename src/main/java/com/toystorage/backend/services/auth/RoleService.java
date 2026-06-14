package com.toystorage.backend.services.auth;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.Role;
import com.toystorage.backend.repository.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new BadRequest("ROLE_NOT_FOUND"));
    }

    public Role getRoleByCode(String code) {
        return (Role) roleRepository.findByCode(code)
                .orElseThrow(() -> new BadRequest("ROLE_NOT_FOUND"));
    }

    public Role createRole(Role role) {
        if (role == null) {
            throw new BadRequest("ROLE_REQUIRED");
        }

        if (role.getCode() == null || role.getCode().isBlank()) {
            throw new BadRequest("ROLE_CODE_REQUIRED");
        }

        if (role.getName() == null || role.getName().isBlank()) {
            throw new BadRequest("ROLE_NAME_REQUIRED");
        }

        String code = role.getCode().trim().toUpperCase();

        if (roleRepository.existsByCode(code)) {
            throw new BadRequest("ROLE_CODE_EXISTS");
        }

        role.setCode(code);
        role.setName(role.getName().trim());

        if (role.getDescription() != null) {
            role.setDescription(role.getDescription().trim());
        }

        return roleRepository.save(role);
    }

    public Role updateRole(Long id, Role role) {
        Role oldRole = getRoleById(id);

        if (role == null) {
            throw new BadRequest("ROLE_REQUIRED");
        }

        if (role.getCode() != null && !role.getCode().isBlank()) {
            String code = role.getCode().trim().toUpperCase();

            if (!code.equals(oldRole.getCode()) && roleRepository.existsByCode(code)) {
                throw new BadRequest("ROLE_CODE_EXISTS");
            }

            oldRole.setCode(code);
        }

        if (role.getName() != null && !role.getName().isBlank()) {
            oldRole.setName(role.getName().trim());
        }

        if (role.getDescription() != null) {
            oldRole.setDescription(role.getDescription().trim());
        }

        return roleRepository.save(oldRole);
    }

    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }
}