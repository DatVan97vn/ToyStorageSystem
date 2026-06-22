package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.StockTransferItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockTransferItemRepository
        extends JpaRepository<StockTransferItems, Long> {

    List<StockTransferItems> findByTransfer_Id(Long transferId);

    Optional<StockTransferItems> findByTransfer_IdAndProduct_Barcode(
            Long transferId,
            String barcode
    );
}