package com.toystorage.backend.models.warehouses;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_locations")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class WarehouseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private String zone;



    private String aisle;


    private String shelf;

    @Column(name = "level_code")
    private String levelCode;

    @Column(name = "bin_code")
    private String binCode;

    @Column(name = "location_code", unique = true)
    private String locationCode;

    @Column(name = "location_barcode", unique = true)
    private String locationBarcode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}