package com.toystorage.backend.models.packages;

import com.toystorage.backend.enums.packages.PackageStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.warehouses.Warehouses;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "packages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_code", unique = true)
    private String packageCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private StockTransfer transfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;

    @Enumerated(EnumType.STRING)
    private PackageStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packed_by")
    private User packedBy;

    @Column(name = "packed_at")
    private LocalDateTime packedAt;

    @Column(name = "sealed_at")
    private LocalDateTime sealedAt;

    private BigDecimal weight;

    @Column(columnDefinition = "TEXT")
    private String note;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = PackageStatus.OPEN;
        }

        this.packedAt = LocalDateTime.now();
    }
}