package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.TransferScans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferScanRepository
        extends JpaRepository<TransferScans, Long> {
}