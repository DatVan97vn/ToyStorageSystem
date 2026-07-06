package com.toystorage.backend.controllers.receipts;

import com.toystorage.backend.dto.request.receipts.CreateGoodsReceiptRequest;
import com.toystorage.backend.dto.request.receipts.ReceiveGoodsReceiptItemRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.receipts.GoodsReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods-receipts")
@RequiredArgsConstructor
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    @PostMapping
    public ResponseEntity<?> createReceipt(
            @RequestBody CreateGoodsReceiptRequest request
    ) {
        if (request == null) {
            throw new BadRequest("GOODS_RECEIPT_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.createReceipt(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllReceipts() {
        return ResponseEntity.ok(
                goodsReceiptService.getAllReceipts()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReceiptById(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.getReceiptById(id)
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<?> getReceiptsByWarehouse(
            @PathVariable Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.getReceiptsByWarehouse(warehouseId)
        );
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<?> getReceiptItems(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.getReceiptItems(id)
        );
    }

    @PutMapping("/{id}/requested")
    public ResponseEntity<?> markRequested(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.markRequested(id)
        );
    }

    @PutMapping("/{id}/ordering")
    public ResponseEntity<?> markOrdering(
            @PathVariable Long id,
            @RequestParam Long businessStaffId
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        if (businessStaffId == null) {
            throw new BadRequest("BUSINESS_STAFF_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.markOrdering(id, businessStaffId)
        );
    }

    @PutMapping("/{id}/delivering")
    public ResponseEntity<?> markDelivering(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.markDelivering(id)
        );
    }

    @PutMapping("/{id}/arrived")
    public ResponseEntity<?> markArrived(
            @PathVariable Long id,
            @RequestParam Long receivedById
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        if (receivedById == null) {
            throw new BadRequest("RECEIVER_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.markArrived(id, receivedById)
        );
    }

    @PutMapping("/{id}/checking")
    public ResponseEntity<?> startChecking(
            @PathVariable Long id,
            @RequestParam Long checkedById
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        if (checkedById == null) {
            throw new BadRequest("CHECKER_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.startChecking(id, checkedById)
        );
    }

    @PutMapping("/items/{itemId}/receive")
    public ResponseEntity<?> receiveItem(
            @PathVariable Long itemId,
            @RequestBody ReceiveGoodsReceiptItemRequest request
    ) {
        if (itemId == null) {
            throw new BadRequest("GOODS_RECEIPT_ITEM_ID_REQUIRED");
        }

        if (request == null) {
            throw new BadRequest("RECEIVE_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.receiveItem(itemId, request)
        );
    }

    @PutMapping("/{id}/complete-checking")
    public ResponseEntity<?> completeChecking(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.completeChecking(id)
        );
    }

    @PutMapping("/{id}/putaway")
    public ResponseEntity<?> moveToPutaway(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.moveToPutaway(id)
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeReceipt(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.completeReceipt(id)
        );
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReceipt(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.cancelReceipt(id)
        );
    }
}