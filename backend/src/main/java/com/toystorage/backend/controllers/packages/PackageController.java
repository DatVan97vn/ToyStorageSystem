package com.toystorage.backend.controllers.packages;

import com.toystorage.backend.services.packages.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * API kiện hàng
 */

@RestController

@RequestMapping("/api/packages")

@RequiredArgsConstructor

public class PackageController {

    private final PackageService packageService;

    /*
     * Tạo kiện hàng
     */
    @PostMapping

    public ResponseEntity<?> createPackage() {

        return ResponseEntity.ok(
                packageService.createPackage()
        );
    }

    /*
     * Danh sách kiện
     */
    @GetMapping

    public ResponseEntity<?> getAllPackages() {

        return ResponseEntity.ok(
                packageService.getAllPackages()
        );
    }
}