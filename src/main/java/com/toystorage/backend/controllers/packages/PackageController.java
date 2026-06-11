package com.toystorage.backend.controllers.packages;

import com.toystorage.backend.exceptions.BadRequest;
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
     * POST /api/packages
     */
    @PostMapping
    public ResponseEntity<?> createPackage() {

        Object createdPackage = packageService.createPackage();

        if (createdPackage == null) {
            throw new BadRequest("PACKAGE_CREATE_FAILED");
        }

        return ResponseEntity.ok(createdPackage);
    }

    /*
     * Danh sách kiện hàng
     * GET /api/packages
     */
    @GetMapping
    public ResponseEntity<?> getAllPackages() {

        return ResponseEntity.ok(
                packageService.getAllPackages()
        );
    }
}