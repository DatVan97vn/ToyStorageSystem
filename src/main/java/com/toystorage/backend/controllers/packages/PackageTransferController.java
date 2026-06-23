package com.toystorage.backend.controllers.packages;

import com.toystorage.backend.dto.request.packages.PackageTransferRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.packages.PackageTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/package-transfers")
@RequiredArgsConstructor

public class PackageTransferController {

    private final PackageTransferService packageTransferService;

    /*
     * Thêm item điều kho vào kiện
     */
    @PostMapping
    public ResponseEntity<?> addItemToPackage(
            @RequestBody PackageTransferRequest request
    ) {
        if (request == null) {
            throw new BadRequest("PACKAGE_TRANSFER_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                packageTransferService.addItemToPackage(request)
        );
    }

    /*
     * Xem các item trong một kiện
     */
    @GetMapping("/package/{packageId}")
    public ResponseEntity<?> getItemsByPackage(
            @PathVariable Long packageId
    ) {
        return ResponseEntity.ok(
                packageTransferService.getItemsByPackage(packageId)
        );
    }

    /*
     * Xem các kiện/item theo phiếu điều kho
     */
    @GetMapping("/transfer/{transferId}")
    public ResponseEntity<?> getItemsByTransfer(
            @PathVariable Long transferId
    ) {
        return ResponseEntity.ok(
                packageTransferService.getItemsByTransfer(transferId)
        );
    }

    /*
     * Xem item điều kho đã nằm trong những kiện nào
     */
    @GetMapping("/transfer-item/{transferItemId}")
    public ResponseEntity<?> getItemsByTransferItem(
            @PathVariable Long transferItemId
    ) {
        return ResponseEntity.ok(
                packageTransferService.getItemsByTransferItem(transferItemId)
        );
    }

    /*
     * Xóa item khỏi kiện
     */
    @DeleteMapping("/package/{packageId}/transfer-item/{transferItemId}")
    public ResponseEntity<?> removeItemFromPackage(
            @PathVariable Long packageId,
            @PathVariable Long transferItemId
    ) {
        packageTransferService.removeItemFromPackage(
                packageId,
                transferItemId
        );

        return ResponseEntity.ok("REMOVE_ITEM_FROM_PACKAGE_SUCCESS");
    }
}