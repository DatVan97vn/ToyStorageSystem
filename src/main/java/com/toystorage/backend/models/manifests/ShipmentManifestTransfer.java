package com.toystorage.backend.models.manifests;

import com.toystorage.backend.models.transfers.StockTransfer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipment_manifest_transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentManifestTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manifest_id", nullable = false)
    private ShipmentManifest manifest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private StockTransfer transfer;
}