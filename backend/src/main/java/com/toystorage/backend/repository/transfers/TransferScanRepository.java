package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.TransferScan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferScanRepository
        extends JpaRepository<TransferScan, Long> {
}