package com.toystorage.backend.models.receipts;

import com.toystorage.backend.enums.receipts.ReceiptStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.warehouses.Warehouses;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "goods_receipts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_code", unique = true)
    private String receiptCode;

    @Column(name = "manifest_id")
    private Long manifestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private StockTransfer transfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by")
    private User receivedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by")
    private User checkedBy;

    @Enumerated(EnumType.STRING)
    private ReceiptStatus status;

    @Column(name = "started_receive_at")
    private LocalDateTime startedReceiveAt;

    @Column(name = "completed_receive_at")
    private LocalDateTime completedReceiveAt;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = ReceiptStatus.DRAFT;
        }
    }
}