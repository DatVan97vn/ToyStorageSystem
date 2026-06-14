package com.toystorage.backend.controllers.receipts;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.receipts.GoodsReceiptScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/goods-receipt-scans")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GoodsReceiptScanController {

    private final GoodsReceiptScanService goodsReceiptScanService;

    @PostMapping
    public ResponseEntity<?> scanBarcode(
            @RequestBody Map<String, Object> payload
    ) {
        if (payload == null) {
            throw new BadRequest("PAYLOAD_REQUIRED");
        }

        Long goodsReceiptId = Long.valueOf(
                payload.get("goodsReceiptId").toString()
        );

        String barcode = payload.get("barcode").toString();

        Integer quantity = Integer.valueOf(
                payload.get("quantity").toString()
        );

        Long scannedById = Long.valueOf(
                payload.get("scannedById").toString()
        );

        return ResponseEntity.ok(
                goodsReceiptScanService.scanBarcode(
                        goodsReceiptId,
                        barcode,
                        quantity,
                        scannedById
                )
        );
    }

    @GetMapping("/receipt/{goodsReceiptId}")
    public ResponseEntity<?> getScansByReceipt(
            @PathVariable Long goodsReceiptId
    ) {
        return ResponseEntity.ok(
                goodsReceiptScanService.getScansByReceipt(goodsReceiptId)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getScansByProduct(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(
                goodsReceiptScanService.getScansByProduct(productId)
        );
    }
}