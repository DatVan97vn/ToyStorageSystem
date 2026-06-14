package com.toystorage.backend.services.receipts;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.receipts.GoodsReceipt;
import com.toystorage.backend.models.receipts.GoodsReceiptItem;
import com.toystorage.backend.models.receipts.GoodsReceiptScan;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.products.ProductRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptItemRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptScanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsReceiptScanService {

    private final GoodsReceiptScanRepository goodsReceiptScanRepository;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptItemRepository goodsReceiptItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public GoodsReceiptScan scanBarcode(
            Long goodsReceiptId,
            String barcode,
            Integer quantity,
            Long scannedById
    ) {
        if (goodsReceiptId == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        if (barcode == null || barcode.isBlank()) {
            throw new BadRequest("BARCODE_REQUIRED");
        }

        if (quantity == null || quantity <= 0) {
            throw new BadRequest("QUANTITY_INVALID");
        }

        if (scannedById == null) {
            throw new BadRequest("SCANNED_BY_REQUIRED");
        }

        GoodsReceipt receipt = goodsReceiptRepository.findById(goodsReceiptId)
                .orElseThrow(() -> new BadRequest("GOODS_RECEIPT_NOT_FOUND"));

        Products product = productRepository.findByBarcode(barcode.trim())
                .orElseThrow(() -> new BadRequest("PRODUCT_NOT_FOUND"));

        User scannedBy = userRepository.findById(scannedById)
                .orElseThrow(() -> new BadRequest("USER_NOT_FOUND"));

        List<GoodsReceiptItem> items =
                goodsReceiptItemRepository.findByGoodsReceipt_Id(goodsReceiptId);

        GoodsReceiptItem matchedItem = null;

        for (GoodsReceiptItem item : items) {
            if (
                    item.getProduct() != null
                            && item.getProduct().getId().equals(product.getId())
            ) {
                matchedItem = item;
                break;
            }
        }

        if (matchedItem == null) {
            throw new BadRequest("PRODUCT_NOT_IN_RECEIPT");
        }

        Integer currentReceived =
                matchedItem.getReceivedQuantity() != null
                        ? matchedItem.getReceivedQuantity()
                        : 0;

        matchedItem.setReceivedQuantity(currentReceived + quantity);

        goodsReceiptItemRepository.save(matchedItem);

        GoodsReceiptScan scan = GoodsReceiptScan.builder()
                .goodsReceipt(receipt)
                .goodsReceiptItem(matchedItem)
                .product(product)
                .barcode(barcode.trim())
                .scannedQuantity(quantity)
                .scannedBy(scannedBy)
                .build();

        return goodsReceiptScanRepository.save(scan);
    }

    public List<GoodsReceiptScan> getScansByReceipt(Long goodsReceiptId) {
        if (goodsReceiptId == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return goodsReceiptScanRepository.findByGoodsReceipt_Id(goodsReceiptId);
    }

    public List<GoodsReceiptScan> getScansByProduct(Long productId) {
        if (productId == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        return goodsReceiptScanRepository.findByProduct_Id(productId);
    }
}