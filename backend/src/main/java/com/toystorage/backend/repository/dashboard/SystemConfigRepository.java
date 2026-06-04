package com.toystorage.backend.repository.dashboard;

import com.toystorage.backend.models.dashboard.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    // Hàm tìm kiếm cấu hình theo Key (Mã thùng hoặc Mã phiếu)
    Optional<SystemConfig> findByConfigKey(String configKey);
}