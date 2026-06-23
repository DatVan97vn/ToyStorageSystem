package com.toystorage.backend.controllers.packages;

import com.toystorage.backend.dto.request.packages.CreatePackageRequest;
import com.toystorage.backend.dto.response.packages.PackageResponse;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.packages.PackageMapper;
import com.toystorage.backend.services.packages.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor

public class PackageController {

    private final PackageService packageService;

    @PostMapping
    public ResponseEntity<?> createPackages(
            @RequestBody CreatePackageRequest request
    ) {
        if (request == null) {
            throw new BadRequest("PACKAGE_REQUEST_REQUIRED");
        }

        List<PackageResponse> responses =
                packageService.createPackages(request)
                        .stream()
                        .map(PackageMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<?> getAllPackages() {
        List<PackageResponse> responses =
                packageService.getAllPackages()
                        .stream()
                        .map(PackageMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                PackageMapper.toResponse(
                        packageService.getPackageById(id)
                )
        );
    }

    @GetMapping("/transfer/{transferId}")
    public ResponseEntity<?> getPackagesByTransfer(
            @PathVariable Long transferId
    ) {
        List<PackageResponse> responses =
                packageService.getPackagesByTransfer(transferId)
                        .stream()
                        .map(PackageMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }
    @PutMapping("/{id}/close")
    public ResponseEntity<?> closePackage(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("PACKAGE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                PackageMapper.toResponse(
                        packageService.closePackage(id)
                )
        );
    }
}