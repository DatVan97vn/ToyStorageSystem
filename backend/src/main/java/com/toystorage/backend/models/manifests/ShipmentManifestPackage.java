package com.toystorage.backend.models.manifests;

import com.toystorage.backend.models.packages.PackageBox;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipment_manifest_packages")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShipmentManifestPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Bảng kê
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "manifest_id")
    private ShipmentManifest manifest;

    /*
     * Thùng hàng
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "package_id")
    private PackageBox packageBox;
}