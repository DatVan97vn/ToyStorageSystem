package com.toystorage.backend.repository.packages;

import com.toystorage.backend.models.packages.PackageTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageTransferItemRepository
        extends JpaRepository<PackageTransferItem, Long> {
}