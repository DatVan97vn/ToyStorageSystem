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

    /*
     * Tạo phiếu nhận hàng
     */
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

    /*
     * Xem toàn bộ phiếu nhận hàng
     */
    @GetMapping
    public ResponseEntity<?> getAllReceipts() {

        return ResponseEntity.ok(
                goodsReceiptService.getAllReceipts()
        );
    }

    /*
     * Xem chi tiết phiếu nhận hàng
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReceiptById(
            @PathVariable Long id
    ) {

        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        Object receipt = goodsReceiptService.getReceiptById(id);

        if (receipt == null) {
            throw new BadRequest("GOODS_RECEIPT_NOT_FOUND");
        }

        return ResponseEntity.ok(receipt);
    }

    /*
     * Xem phiếu theo kho
     */
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

    /*
     * Danh sách item trong phiếu
     */
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

    /*
     * Bắt đầu nhận hàng
     */
    @PutMapping("/{id}/start")
    public ResponseEntity<?> startReceiving(
            @PathVariable Long id
    ) {

        if (id == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                goodsReceiptService.startReceiving(id)
        );
    }

    /*
     * Cập nhật số lượng thực nhận
     */
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
                goodsReceiptService.receiveItem(
                        itemId,
                        request
                )
        );
    }

    /*
     * Hoàn tất phiếu
     */
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

    /*
     * Hủy phiếu
     */
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