package com.toystorage.backend.controllers.packages;

import com.toystorage.backend.dto.request.packages.CreatePackageRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.packages.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PackageController {

    private final PackageService packageService;

    @PostMapping
    public ResponseEntity<?> createPackages(
            @RequestBody CreatePackageRequest request
    ) {
        if (request == null) {
            throw new BadRequest("PACKAGE_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                packageService.createPackages(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllPackages() {
        return ResponseEntity.ok(
                packageService.getAllPackages()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                packageService.getPackageById(id)
        );
    }

    @GetMapping("/transfer/{transferId}")
    public ResponseEntity<?> getPackagesByTransfer(
            @PathVariable Long transferId
    ) {
        return ResponseEntity.ok(
                packageService.getPackagesByTransfer(transferId)
        );
    }
}