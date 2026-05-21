package com.toystorage.backend.models.manifests;

import com.toystorage.backend.enums.manifests.ManifestStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.warehouses.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_manifests")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShipmentManifest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Mã bảng kê
     */
    @Column(name = "manifest_code", unique = true)
    private String manifestCode;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "from_warehouse_id")
    private Warehouse fromWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "to_warehouse_id")
    private Warehouse toWarehouse;

    /*
     * Trạng thái bảng kê
     */
    @Enumerated(EnumType.STRING)

    private ManifestStatus status;

    /*
     * Người tạo bảng kê
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        if (this.status == null) {

            this.status = ManifestStatus.DRAFT;
        }
    }
}