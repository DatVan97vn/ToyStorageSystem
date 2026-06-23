package com.toystorage.backend.controllers.dashboard;

import com.toystorage.backend.exceptions.BadRequest;
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

    // GET /api/dashboard/config/CONFIG_MA_THUNG
    @GetMapping("/{configKey}")
    public ResponseEntity<SystemConfig> getConfig(
            @PathVariable String configKey
    ) {
        if (configKey == null || configKey.isBlank()) {
            throw new BadRequest("CONFIG_KEY_REQUIRED");
        }

        SystemConfig config = systemConfigService.getConfig(configKey);

        if (config == null) {
            throw new BadRequest("CONFIG_NOT_FOUND");
        }

        return ResponseEntity.ok(config);
    }

    // PUT /api/dashboard/config/CONFIG_MA_THUNG
    @PutMapping("/{configKey}")
    public ResponseEntity<SystemConfig> updateConfig(
            @PathVariable String configKey,
            @RequestBody Map<String, Object> payload
    ) {
        if (configKey == null || configKey.isBlank()) {
            throw new BadRequest("CONFIG_KEY_REQUIRED");
        }

        if (payload == null) {
            throw new BadRequest("PAYLOAD_REQUIRED");
        }

        String prefix = (String) payload.get("prefix");

        Object codeLengthValue = payload.get("codeLength");

        if (prefix == null || prefix.isBlank()) {
            throw new BadRequest("PREFIX_REQUIRED");
        }

        if (codeLengthValue == null) {
            throw new BadRequest("CODE_LENGTH_REQUIRED");
        }

        Integer codeLength;

        try {
            codeLength = Integer.valueOf(codeLengthValue.toString());
        } catch (NumberFormatException e) {
            throw new BadRequest("CODE_LENGTH_INVALID");
        }

        if (codeLength <= 0) {
            throw new BadRequest("CODE_LENGTH_MUST_BE_POSITIVE");
        }

        SystemConfig updated = systemConfigService.updateConfig(
                configKey,
                prefix.trim(),
                codeLength
        );

        return ResponseEntity.ok(updated);
    }
}