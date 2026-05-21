package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.StockTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferItemRepository
        extends JpaRepository<StockTransferItem, Long> {
}