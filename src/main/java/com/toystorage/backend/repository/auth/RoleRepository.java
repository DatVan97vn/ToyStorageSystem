package com.toystorage.backend.repository.auth;

import com.toystorage.backend.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Thêm duy nhất dòng này để tìm kiếm Role theo tên (Ví dụ: "ADMIN")
    Optional<Role> findByName(String name);
}