package com.toystorage.backend.repository.receipts;

import com.toystorage.backend.models.receipts.GoodsReceiptScan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsReceiptScanRepository
        extends JpaRepository<GoodsReceiptScan, Long> {

    List<GoodsReceiptScan> findByGoodsReceipt_Id(Long goodsReceiptId);

    List<GoodsReceiptScan> findByGoodsReceiptItem_Id(Long goodsReceiptItemId);

    List<GoodsReceiptScan> findByProduct_Id(Long productId);
}