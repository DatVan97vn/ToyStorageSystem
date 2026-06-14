package com.toystorage.backend.services.packages;

import com.toystorage.backend.dto.request.packages.PackageTransferRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.packages.PackageBox;
import com.toystorage.backend.models.packages.PackageTransferItem;
import com.toystorage.backend.models.transfers.StockTransferItems;
import com.toystorage.backend.repository.packages.PackageBoxRepository;
import com.toystorage.backend.repository.packages.PackageTransferItemRepository;
import com.toystorage.backend.repository.transfers.StockTransferItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageTransferService {

    private final PackageBoxRepository packageBoxRepository;
    private final PackageTransferItemRepository packageTransferItemRepository;
    private final StockTransferItemRepository stockTransferItemRepository;

    public PackageTransferItem addItemToPackage(
            PackageTransferRequest request
    ) {
        if (request == null) {
            throw new BadRequest("PACKAGE_TRANSFER_REQUEST_REQUIRED");
        }

        if (request.getPackageId() == null) {
            throw new BadRequest("PACKAGE_ID_REQUIRED");
        }

        if (request.getTransferItemId() == null) {
            throw new BadRequest("TRANSFER_ITEM_ID_REQUIRED");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequest("QUANTITY_INVALID");
        }

        PackageBox packageBox = packageBoxRepository
                .findById(request.getPackageId())
                .orElseThrow(() -> new BadRequest("PACKAGE_NOT_FOUND"));

        StockTransferItems transferItem = stockTransferItemRepository
                .findById(request.getTransferItemId())
                .orElseThrow(() -> new BadRequest("TRANSFER_ITEM_NOT_FOUND"));

        if (
                packageTransferItemRepository
                        .existsByPackageBoxIdAndTransferItemId(
                                request.getPackageId(),
                                request.getTransferItemId()
                        )
        ) {
            throw new BadRequest("TRANSFER_ITEM_ALREADY_IN_PACKAGE");
        }

        PackageTransferItem item = PackageTransferItem.builder()
                .packageBox(packageBox)
                .transfer(transferItem.getTransfer())
                .transferItem(transferItem)
                .product(transferItem.getProduct())
                .quantity(request.getQuantity())
                .build();

        return packageTransferItemRepository.save(item);
    }

    public List<PackageTransferItem> getItemsByPackage(
            Long packageId
    ) {
        if (packageId == null) {
            throw new BadRequest("PACKAGE_ID_REQUIRED");
        }

        return packageTransferItemRepository.findByPackageBoxId(packageId);
    }

    public List<PackageTransferItem> getItemsByTransfer(
            Long transferId
    ) {
        if (transferId == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        return packageTransferItemRepository.findByTransferId(transferId);
    }

    public List<PackageTransferItem> getItemsByTransferItem(
            Long transferItemId
    ) {
        if (transferItemId == null) {
            throw new BadRequest("TRANSFER_ITEM_ID_REQUIRED");
        }

        return packageTransferItemRepository.findByTransferItemId(transferItemId);
    }

    public void removeItemFromPackage(
            Long packageId,
            Long transferItemId
    ) {
        if (packageId == null) {
            throw new BadRequest("PACKAGE_ID_REQUIRED");
        }

        if (transferItemId == null) {
            throw new BadRequest("TRANSFER_ITEM_ID_REQUIRED");
        }

        PackageTransferItem item = packageTransferItemRepository
                .findByPackageBoxIdAndTransferItemId(
                        packageId,
                        transferItemId
                )
                .orElseThrow(() ->
                        new BadRequest("PACKAGE_TRANSFER_ITEM_NOT_FOUND")
                );

        packageTransferItemRepository.delete(item);
    }
}