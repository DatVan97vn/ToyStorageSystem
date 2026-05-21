package com.toystorage.backend.models.transfers;

import com.toystorage.backend.enums.transfers.ScanType;
import com.toystorage.backend.models.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_scans")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransferScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Item được scan
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "transfer_item_id")
    private StockTransferItem transferItem;

    /*
     * Barcode scan
     */
    private String barcode;

    /*
     * Số lượng scan
     */
    private Integer quantity = 1;

    /*
     * PICK / RECEIVE
     */
    @Enumerated(EnumType.STRING)

    @Column(name = "scan_type")
    private ScanType scanType;

    /*
     * Người scan
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "scanned_by")
    private User scannedBy;

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;

    @PrePersist
    public void prePersist() {

        this.scannedAt = LocalDateTime.now();
    }
}