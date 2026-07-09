package com.toystorage.backend.services.packages;

import com.toystorage.backend.dto.request.packages.CreatePackageRequest;
import com.toystorage.backend.enums.packages.PackageStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.packages.PackageBox;
import com.toystorage.backend.models.packages.PackageTransferItem;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.packages.PackageBoxRepository;
import com.toystorage.backend.repository.packages.PackageTransferItemRepository;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageBoxRepository packageBoxRepository;
    private final PackageTransferItemRepository packageTransferItemRepository;
    private final WarehouseRepository warehouseRepository;

    public List<PackageBox> createPackages(CreatePackageRequest request) {
        if (request == null) {
            throw new BadRequest("PACKAGE_REQUEST_REQUIRED");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequest("PACKAGE_QUANTITY_INVALID");
        }

        Warehouses warehouse = null;

        if (request.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));
        }

        List<PackageBox> boxes = new ArrayList<>();

        for (int i = 0; i < request.getQuantity(); i++) {
            PackageBox box = PackageBox.builder()
                    .packageCode(generatePackageCode())
                    .warehouse(warehouse)
                    .status(PackageStatus.OPEN)
                    .note(request.getNote())
                    .build();

            boxes.add(packageBoxRepository.save(box));
        }

        return boxes;
    }

    public List<PackageBox> getAllPackages() {
        return packageBoxRepository.findAll();
    }

    public List<PackageBox> getPackagesByTransfer(Long transferId) {
        if (transferId == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        return packageTransferItemRepository.findByTransferId(transferId)
                .stream()
                .map(PackageTransferItem::getPackageBox)
                .distinct()
                .toList();
    }

    public PackageBox getPackageById(Long id) {
        return packageBoxRepository.findById(id)
                .orElseThrow(() -> new BadRequest("PACKAGE_NOT_FOUND"));
    }

    public PackageBox closePackage(Long id) {
        PackageBox box = packageBoxRepository.findById(id)
                .orElseThrow(() -> new BadRequest("PACKAGE_NOT_FOUND"));

        box.setStatus(PackageStatus.SEALED);
        box.setSealedAt(LocalDateTime.now());

        return packageBoxRepository.save(box);
    }

    private String generatePackageCode() {
        String code = "BOX-" + System.currentTimeMillis();

        while (packageBoxRepository.findByPackageCode(code).isPresent()) {
            code = "BOX-" + System.nanoTime();
        }

        return code;
    }
}