package com.toystorage.backend.services.manifests;

import com.toystorage.backend.dto.response.manifests.ManifestPackageResponse;
import com.toystorage.backend.dto.response.manifests.ManifestTransferResponse;
import com.toystorage.backend.dto.response.manifests.ShipmentManifestResponse;
import com.toystorage.backend.enums.manifests.ManifestStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.manifests.ManifestPackageMapper;
import com.toystorage.backend.mapper.manifests.ManifestTransferMapper;
import com.toystorage.backend.mapper.manifests.ShipmentManifestMapper;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.manifests.ShipmentManifest;
import com.toystorage.backend.models.manifests.ShipmentManifestPackage;
import com.toystorage.backend.models.manifests.ShipmentManifestTransfer;
import com.toystorage.backend.models.packages.PackageBox;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.manifests.ShipmentManifestPackageRepository;
import com.toystorage.backend.repository.manifests.ShipmentManifestRepository;
import com.toystorage.backend.repository.manifests.ShipmentManifestTransferRepository;
import com.toystorage.backend.repository.packages.PackageBoxRepository;
import com.toystorage.backend.repository.transfers.StockTransferRepository;
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
    private final ShipmentManifestTransferRepository shipmentManifestTransferRepository;

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final PackageBoxRepository packageBoxRepository;
    private final StockTransferRepository stockTransferRepository;

    @Transactional
    public ShipmentManifestResponse createManifest(
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

        return ShipmentManifestMapper.toResponse(
                shipmentManifestRepository.save(manifest)
        );
    }

    public ShipmentManifest getManifestById(Long id) {
        if (id == null) {
            throw new BadRequest("MANIFEST_ID_REQUIRED");
        }

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

    @Transactional(readOnly = true)
    public List<ShipmentManifestResponse> getAllManifestResponses() {
        return shipmentManifestRepository.findAll()
                .stream()
                .map(ShipmentManifestMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ShipmentManifestResponse getManifestResponseById(Long id) {
        ShipmentManifest manifest =
                shipmentManifestRepository.findDetailById(id)
                        .orElseThrow(() ->
                                new BadRequest("MANIFEST_NOT_FOUND")
                        );

        return ShipmentManifestMapper.toResponse(manifest);
    }

    @Transactional(readOnly = true)
    public ShipmentManifestResponse getManifestResponseByCode(
            String manifestCode
    ) {
        return ShipmentManifestMapper.toResponse(
                getManifestByCode(manifestCode)
        );
    }

    @Transactional(readOnly = true)
    public List<ShipmentManifestResponse> getManifestResponsesByFromWarehouse(
            Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return shipmentManifestRepository.findByFromWarehouse_Id(warehouseId)
                .stream()
                .map(ShipmentManifestMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ShipmentManifestResponse> getManifestResponsesByToWarehouse(
            Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return shipmentManifestRepository.findByToWarehouse_Id(warehouseId)
                .stream()
                .map(ShipmentManifestMapper::toResponse)
                .toList();
    }

    @Transactional
    public ShipmentManifestResponse updateStatus(
            Long id,
            ManifestStatus status
    ) {
        if (id == null) {
            throw new BadRequest("MANIFEST_ID_REQUIRED");
        }

        if (status == null) {
            throw new BadRequest("MANIFEST_STATUS_REQUIRED");
        }

        ShipmentManifest manifest = getManifestById(id);

        manifest.setStatus(status);

        return ShipmentManifestMapper.toResponse(
                shipmentManifestRepository.save(manifest)
        );
    }

    // =========================================================
    // TRANSFERS IN MANIFEST
    // Business tạo manifest rồi gắn transfer vào manifest
    // =========================================================

    @Transactional
    public void addTransferToManifest(
            Long manifestId,
            Long transferId
    ) {
        ShipmentManifest manifest = getManifestById(manifestId);

        StockTransfer transfer = stockTransferRepository.findById(transferId)
                .orElseThrow(() -> new BadRequest("TRANSFER_NOT_FOUND"));

        if (shipmentManifestTransferRepository.existsByManifest_IdAndTransfer_Id(
                manifestId,
                transferId
        )) {
            throw new BadRequest("TRANSFER_ALREADY_IN_MANIFEST");
        }

        ShipmentManifestTransfer manifestTransfer =
                ShipmentManifestTransfer.builder()
                        .manifest(manifest)
                        .transfer(transfer)
                        .build();

        shipmentManifestTransferRepository.save(manifestTransfer);
    }

    @Transactional(readOnly = true)
    public List<ManifestTransferResponse> getTransferResponsesByManifest(
            Long manifestId
    ) {
        if (manifestId == null) {
            throw new BadRequest("MANIFEST_ID_REQUIRED");
        }

        return shipmentManifestTransferRepository.findByManifest_Id(manifestId)
                .stream()
                .map(ManifestTransferMapper::toResponse)
                .toList();
    }

    @Transactional
    public void removeTransferFromManifest(
            Long manifestId,
            Long transferId
    ) {
        if (!shipmentManifestTransferRepository.existsByManifest_IdAndTransfer_Id(
                manifestId,
                transferId
        )) {
            throw new BadRequest("MANIFEST_TRANSFER_NOT_FOUND");
        }

        shipmentManifestTransferRepository.deleteByManifest_IdAndTransfer_Id(
                manifestId,
                transferId
        );
    }

    // =========================================================
    // PACKAGES IN MANIFEST
    // Kho đóng hàng xong mới gắn package vào manifest
    // =========================================================

    @Transactional
    public void addPackageToManifest(
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

        ShipmentManifestPackage manifestPackage =
                ShipmentManifestPackage.builder()
                        .manifest(manifest)
                        .packageBox(packageBox)
                        .build();

        shipmentManifestPackageRepository.save(manifestPackage);
    }

    @Transactional(readOnly = true)
    public List<ManifestPackageResponse> getPackageResponsesByManifest(
            Long manifestId
    ) {
        if (manifestId == null) {
            throw new BadRequest("MANIFEST_ID_REQUIRED");
        }

        return shipmentManifestPackageRepository.findByManifest_Id(manifestId)
                .stream()
                .map(ManifestPackageMapper::toResponse)
                .toList();
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