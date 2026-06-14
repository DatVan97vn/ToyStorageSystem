package com.toystorage.backend.services.manifests;

import com.toystorage.backend.enums.manifests.ManifestStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.manifests.ShipmentManifest;
import com.toystorage.backend.models.manifests.ShipmentManifestPackage;
import com.toystorage.backend.models.packages.PackageBox;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.manifests.ShipmentManifestPackageRepository;
import com.toystorage.backend.repository.manifests.ShipmentManifestRepository;
import com.toystorage.backend.repository.packages.PackageBoxRepository;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentManifestService {

    private final ShipmentManifestRepository shipmentManifestRepository;
    private final ShipmentManifestPackageRepository shipmentManifestPackageRepository;

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final PackageBoxRepository packageBoxRepository;

    public ShipmentManifest createManifest(
            Long fromWarehouseId,
            Long toWarehouseId,
            Long createdById
    ) {
        if (fromWarehouseId == null) {
            throw new BadRequest("FROM_WAREHOUSE_ID_REQUIRED");
        }

        if (toWarehouseId == null) {
            throw new BadRequest("TO_WAREHOUSE_ID_REQUIRED");
        }

        if (createdById == null) {
            throw new BadRequest("CREATED_BY_ID_REQUIRED");
        }

        Warehouses fromWarehouse = warehouseRepository.findById(fromWarehouseId)
                .orElseThrow(() -> new BadRequest("FROM_WAREHOUSE_NOT_FOUND"));

        Warehouses toWarehouse = warehouseRepository.findById(toWarehouseId)
                .orElseThrow(() -> new BadRequest("TO_WAREHOUSE_NOT_FOUND"));

        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new BadRequest("USER_NOT_FOUND"));

        ShipmentManifest manifest = ShipmentManifest.builder()
                .manifestCode("MF-" + System.currentTimeMillis())
                .fromWarehouse(fromWarehouse)
                .toWarehouse(toWarehouse)
                .createdBy(createdBy)
                .status(ManifestStatus.DRAFT)
                .build();

        return shipmentManifestRepository.save(manifest);
    }

    public List<ShipmentManifest> getAllManifests() {
        return shipmentManifestRepository.findAll();
    }

    public ShipmentManifest getManifestById(Long id) {
        return shipmentManifestRepository.findById(id)
                .orElseThrow(() -> new BadRequest("MANIFEST_NOT_FOUND"));
    }

    public ShipmentManifest getManifestByCode(String manifestCode) {
        if (manifestCode == null || manifestCode.isBlank()) {
            throw new BadRequest("MANIFEST_CODE_REQUIRED");
        }

        return shipmentManifestRepository.findByManifestCode(manifestCode)
                .orElseThrow(() -> new BadRequest("MANIFEST_NOT_FOUND"));
    }

    public List<ShipmentManifest> getManifestsByFromWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return shipmentManifestRepository.findByFromWarehouse_Id(warehouseId);
    }

    public List<ShipmentManifest> getManifestsByToWarehouse(Long warehouseId) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return shipmentManifestRepository.findByToWarehouse_Id(warehouseId);
    }

    public ShipmentManifest updateStatus(Long id, ManifestStatus status) {
        if (status == null) {
            throw new BadRequest("MANIFEST_STATUS_REQUIRED");
        }

        ShipmentManifest manifest = getManifestById(id);
        manifest.setStatus(status);

        return shipmentManifestRepository.save(manifest);
    }

    @Transactional
    public ShipmentManifestPackage addPackageToManifest(
            Long manifestId,
            Long packageId
    ) {
        ShipmentManifest manifest = getManifestById(manifestId);

        PackageBox packageBox = packageBoxRepository.findById(packageId)
                .orElseThrow(() -> new BadRequest("PACKAGE_NOT_FOUND"));

        if (shipmentManifestPackageRepository.existsByManifest_IdAndPackageBox_Id(
                manifestId,
                packageId
        )) {
            throw new BadRequest("PACKAGE_ALREADY_IN_MANIFEST");
        }

        ShipmentManifestPackage manifestPackage = ShipmentManifestPackage.builder()
                .manifest(manifest)
                .packageBox(packageBox)
                .build();

        return shipmentManifestPackageRepository.save(manifestPackage);
    }

    public List<ShipmentManifestPackage> getPackagesByManifest(Long manifestId) {
        if (manifestId == null) {
            throw new BadRequest("MANIFEST_ID_REQUIRED");
        }

        return shipmentManifestPackageRepository.findByManifest_Id(manifestId);
    }

    @Transactional
    public void removePackageFromManifest(
            Long manifestId,
            Long packageId
    ) {
        if (!shipmentManifestPackageRepository.existsByManifest_IdAndPackageBox_Id(
                manifestId,
                packageId
        )) {
            throw new BadRequest("MANIFEST_PACKAGE_NOT_FOUND");
        }

        shipmentManifestPackageRepository.deleteByManifest_IdAndPackageBox_Id(
                manifestId,
                packageId
        );
    }
}