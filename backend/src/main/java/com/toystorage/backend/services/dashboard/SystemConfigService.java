package com.toystorage.backend.services.dashboard;

import com.toystorage.backend.models.dashboard.SystemConfig;
import com.toystorage.backend.repository.dashboard.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    // Lấy cấu hình ra xem
    public SystemConfig getConfig(String configKey) {
        return systemConfigRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình: " + configKey));
    }

    // Cập nhật lại Prefix hoặc độ dài chuỗi mã
    @Transactional
    public SystemConfig updateConfig(String configKey, String prefix, int codeLength) {
        SystemConfig config = systemConfigRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new RuntimeException("Cấu hình không tồn tại."));

        config.setPrefix(prefix);
        config.setCodeLength(codeLength);

        return systemConfigRepository.save(config);
    }

    // Hàm tự động sinh mã mới và cộng dồn số lên 1 (Dùng cho module Thùng/Phiếu gọi ké)
    @Transactional
    public synchronized String generateNextCode(String configKey) {
        SystemConfig config = systemConfigRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình mã với key: " + configKey));

        long nextValue = config.getCurrentValue() + 1;
        config.setCurrentValue(nextValue);
        systemConfigRepository.save(config);

        String formattedNumber = String.format("%0" + config.getCodeLength() + "d", nextValue);
        return config.getPrefix() + formattedNumber;
    }
}