package com.toystorage.backend.models.inventory;

import com.toystorage.backend.enums.inventory.PutawayStatus;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.receipts.GoodsReceipt;
import com.toystorage.backend.models.receipts.GoodsReceiptItem;
import com.toystorage.backend.models.warehouses.WarehouseLocation;
import com.toystorage.backend.models.warehouses.Warehouses;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "putaway_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutawayTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Phiếu nhập hàng
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id", nullable = false)
    private GoodsReceipt goodsReceipt;

    /*
     * Dòng sản phẩm trong phiếu nhập
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_item_id", nullable = false)
    private GoodsReceiptItem goodsReceiptItem;

    /*
     * Kho nhận hàng
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouses warehouse;

    /*
     * Sản phẩm được cất lên kệ
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    /*
     * Kệ/vị trí cần đưa hàng vào
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private WarehouseLocation location;

    /*
     * Số lượng đưa lên kệ
     */
    @Column(nullable = false)
    private Integer quantity;

    /*
     * Người thực hiện putaway
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "putaway_by")
    private User putawayBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PutawayStatus status;

    @Column(name = "putaway_at")
    private LocalDateTime putawayAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.quantity == null) {
            this.quantity = 0;
        }

        if (this.status == null) {
            this.status = PutawayStatus.PENDING;
        }
    }
}