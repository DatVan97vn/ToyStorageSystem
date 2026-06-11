package com.toystorage.backend.repository.receipts;

import com.toystorage.backend.models.receipts.GoodsReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Repository chi tiết phiếu nhận hàng
 */
public interface GoodsReceiptItemRepository
        extends JpaRepository<GoodsReceiptItem, Long> {

    List<GoodsReceiptItem> findByGoodsReceipt_Id(Long goodsReceiptId);
}