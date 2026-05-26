package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.StockTransferItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferItemRepository
        extends JpaRepository<StockTransferItems, Long> {
}