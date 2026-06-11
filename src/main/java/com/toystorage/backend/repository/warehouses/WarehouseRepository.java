package com.toystorage.backend.repository.warehouses;

import com.toystorage.backend.models.warehouses.Warehouses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouses, Long> {

    Optional<Warehouses> findByCode(String code);

    boolean existsByCode(String code);
}