package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.StockTransferItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTransferItemRepository
        extends JpaRepository<StockTransferItems, Long> {

    List<StockTransferItems> findByTransfer_Id(Long transferId);
}