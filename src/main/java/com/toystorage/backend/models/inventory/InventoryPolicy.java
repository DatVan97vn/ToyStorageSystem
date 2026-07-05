package com.toystorage.backend.models.inventory;

import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.warehouses.Warehouses;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "inventory_policies",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "warehouse_id",
                                "product_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Kho / Store áp dụng chính sách tồn
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouses warehouse;

    /*
     * Sản phẩm áp dụng chính sách tồn
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    /*
     * Tồn tối thiểu
     * Nếu tồn <= số này thì báo sắp hết
     */
    @Column(name = "minimum_quantity", nullable = false)
    private Integer minimumQuantity;

    /*
     * Tồn tối đa
     */
    @Column(name = "maximum_quantity", nullable = false)
    private Integer maximumQuantity;

    /*
     * Số lượng gợi ý nhập thêm
     */
    @Column(name = "reorder_quantity", nullable = false)
    private Integer reorderQuantity;

    /*
     * Có đang bật cảnh báo tồn kho không
     */
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.minimumQuantity == null) {
            this.minimumQuantity = 0;
        }

        if (this.maximumQuantity == null) {
            this.maximumQuantity = 0;
        }

        if (this.reorderQuantity == null) {
            this.reorderQuantity = 0;
        }

        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}