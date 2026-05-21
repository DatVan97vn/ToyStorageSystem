package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.models.inventory.InventoryBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryBalanceRepository
        extends JpaRepository<InventoryBalance, Long> {
}