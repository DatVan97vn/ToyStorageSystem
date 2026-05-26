package com.toystorage.backend.models.inventory;

import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.warehouses.Warehouses;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_balances")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InventoryBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Kho
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "warehouse_id")

    private Warehouses warehouse;

    /*
     * Sản phẩm
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "product_id")

    private Products product;

    /*
     * Số lượng tồn
     */
    private Integer quantity;

    /*
     * Ngày cập nhật
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        updatedAt = LocalDateTime.now();

        if (this.quantity == null) {
            this.quantity = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}