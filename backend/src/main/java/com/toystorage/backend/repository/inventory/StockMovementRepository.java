package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.models.inventory.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {
}