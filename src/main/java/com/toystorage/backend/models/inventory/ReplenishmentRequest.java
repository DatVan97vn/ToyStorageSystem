package com.toystorage.backend.models.inventory;

import com.toystorage.backend.enums.inventory.ReplenishmentStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.warehouses.Warehouses;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "replenishment_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplenishmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Mã yêu cầu
     * RR-202607060001
     */
    @Column(name = "request_code", unique = true)
    private String requestCode;

    /*
     * Store yêu cầu bổ sung
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_warehouse_id", nullable = false)
    private Warehouses requestWarehouse;

    /*
     * Business chọn kho cấp hàng
     * Có thể là Kho tổng hoặc Store khác
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id")
    private Warehouses sourceWarehouse;

    /*
     * Sản phẩm
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    /*
     * Tồn hiện tại của Store
     */
    @Column(name = "current_quantity")
    private Integer currentQuantity;

    /*
     * Số lượng yêu cầu
     */
    @Column(name = "requested_quantity", nullable = false)
    private Integer requestedQuantity;

    /*
     * Số lượng Business duyệt
     */
    @Column(name = "approved_quantity")
    private Integer approvedQuantity;

    /*
     * Người gửi yêu cầu
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by")
    private User requestedBy;

    /*
     * Business xử lý
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    /*
     * Phiếu điều kho được tạo
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private StockTransfer stockTransfer;

    /*
     * Business ghi chú
     */
    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReplenishmentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = ReplenishmentStatus.PENDING;
        }

        if (this.requestCode == null) {
            this.requestCode = "RR-" + System.currentTimeMillis();
        }

        if (this.approvedQuantity == null) {
            this.approvedQuantity = 0;
        }
    }
}