package com.toystorage.backend.repository.warehouses;

import com.toystorage.backend.models.warehouses.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseLocationRepository
        extends JpaRepository<WarehouseLocation, Long> {

    Optional<WarehouseLocation> findByLocationCode(String locationCode);

    Optional<WarehouseLocation> findByLocationBarcode(String locationBarcode);
}