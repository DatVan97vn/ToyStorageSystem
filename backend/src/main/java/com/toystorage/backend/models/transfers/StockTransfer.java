package com.toystorage.backend.models.transfers;

import com.toystorage.backend.enums.transfers.TransferStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.suppliers.Suppliers;
import com.toystorage.backend.models.warehouses.Warehouses;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "stock_transfer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StockTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_code", unique = true)
    private String transferCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Suppliers supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_warehouse_id")
    private Warehouses fromWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_warehouse_id")
    private Warehouses toWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_by")
    private User checkBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Enumerated(EnumType.STRING)

    private TransferStatus status;

    @Column(name = "started_pick_at")
    private LocalDateTime startedPickAt;

    @Column(name = "completed_pick_at")
    private LocalDateTime completedPickAt;

    @Column(name = "started_receive_at")
    private LocalDateTime startedReceiveAt;

    @Column(name = "completed_receive_at")
    private LocalDateTime completedReceiveAt;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        if (this.status == null) {

            this.status = TransferStatus.DRAFT;
        }
    }

}
