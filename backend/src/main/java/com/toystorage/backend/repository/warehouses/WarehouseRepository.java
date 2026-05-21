package com.toystorage.backend.repository.warehouses;

import com.toystorage.backend.models.warehouses.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository
        extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByCode(String code);
}
