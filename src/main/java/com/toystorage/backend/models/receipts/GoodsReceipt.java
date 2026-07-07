package com.toystorage.backend.models.receipts;

import com.toystorage.backend.enums.receipts.ReceiptStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.suppliers.Suppliers;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.models.transfers.StockTransfer;
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

    /*
     * Nhà cung cấp
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Suppliers supplier;

    /*
     * Kho nhận hàng
     * Chỉ nên là MAIN_WAREHOUSE
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouses warehouse;
    /*
     * Phiếu điều chuyển kho
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private StockTransfer transfer;
    /*
     * Trưởng cửa hàng / người tạo yêu cầu nhập
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    /*
     * Nhân viên kinh doanh xử lý order supplier
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_staff_id")
    private User businessStaff;

    /*
     * Người xác nhận hàng đã đến
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by")
    private User receivedBy;

    /*
     * Người kiểm hàng
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by")
    private User checkedBy;

    @Enumerated(EnumType.STRING)
    private ReceiptStatus status;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @Column(name = "delivering_at")
    private LocalDateTime deliveringAt;

    @Column(name = "arrived_at")
    private LocalDateTime arrivedAt;

    @Column(name = "started_receive_at")
    private LocalDateTime startedReceiveAt;

    @Column(name = "completed_receive_at")
    private LocalDateTime completedReceiveAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = ReceiptStatus.DRAFT;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}