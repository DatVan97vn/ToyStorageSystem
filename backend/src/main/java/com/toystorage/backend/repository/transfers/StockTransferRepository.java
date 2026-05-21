package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockTransferRepository
        extends JpaRepository<StockTransfer, Long> {

    Optional<StockTransfer> findByTransferCode(String transferCode);
}