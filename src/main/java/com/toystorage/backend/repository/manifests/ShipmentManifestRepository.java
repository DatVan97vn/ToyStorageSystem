package com.toystorage.backend.repository.manifests;

import com.toystorage.backend.models.manifests.ShipmentManifest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShipmentManifestRepository
        extends JpaRepository<ShipmentManifest, Long> {

    Optional<ShipmentManifest> findByManifestCode(String manifestCode);

    List<ShipmentManifest> findByFromWarehouse_Id(Long warehouseId);

    List<ShipmentManifest> findByToWarehouse_Id(Long warehouseId);

    @Query("""
        SELECT m
        FROM ShipmentManifest m
        LEFT JOIN FETCH m.fromWarehouse
        LEFT JOIN FETCH m.toWarehouse
        LEFT JOIN FETCH m.createdBy
        WHERE m.id = :id
    """)
    Optional<ShipmentManifest> findDetailById(
            @Param("id") Long id
    );
}