package com.toystorage.backend.controllers.dashboard;

import com.toystorage.backend.models.dashboard.SystemConfig;
import com.toystorage.backend.services.dashboard.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    // API 1: Lấy cấu hình hiện tại để hiển thị lên Form trên giao diện Admin
    // Đường dẫn: GET /api/dashboard/config/CONFIG_MA_THUNG
    @GetMapping("/{configKey}")
    public ResponseEntity<SystemConfig> getConfig(@PathVariable String configKey) {
        return ResponseEntity.ok(systemConfigService.getConfig(configKey));
    }

    // API 2: Cho phép IT Manager bấm nút Lưu để thay đổi Tiền tố hoặc Độ dài mã
    // Đường dẫn: PUT /api/dashboard/config/CONFIG_MA_THUNG
    @PutMapping("/{configKey}")
    public ResponseEntity<SystemConfig> updateConfig(
            @PathVariable String configKey,
            @RequestBody Map<String, Object> payload
    ) {
        String prefix = (String) payload.get("prefix");
        Integer codeLength = (Integer) payload.get("codeLength");

        SystemConfig updated = systemConfigService.updateConfig(configKey, prefix, codeLength);
        return ResponseEntity.ok(updated);
    }
}