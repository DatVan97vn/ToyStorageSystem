package com.toystorage.backend.repository.receipts;

import com.toystorage.backend.models.receipts.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Repository phiếu nhận hàng
 */
public interface GoodsReceiptRepository
        extends JpaRepository<GoodsReceipt, Long> {

    List<GoodsReceipt> findByWarehouse_Id(Long warehouseId);

    List<GoodsReceipt> findByTransfer_Id(Long transferId);
}