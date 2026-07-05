package com.toystorage.backend.models.inventory;

import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.products.Products;
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
     * Store yêu cầu bổ sung hàng
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouses warehouse;

    /*
     * Sản phẩm cần bổ sung
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    /*
     * Số lượng hiện tại tại store
     */
    @Column(name = "current_quantity")
    private Integer currentQuantity;

    /*
     * Số lượng muốn bổ sung
     */
    @Column(name = "requested_quantity")
    private Integer requestedQuantity;

    /*
     * Lý do yêu cầu
     */
    @Column(columnDefinition = "TEXT")
    private String reason;

    /*
     * Người tạo yêu cầu
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by")
    private User requestedBy;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = "PENDING";
        }
    }
}