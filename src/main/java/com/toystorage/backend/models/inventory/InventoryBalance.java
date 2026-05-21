package com.toystorage.backend.models.inventory;

import com.toystorage.backend.models.products.Product;
import com.toystorage.backend.models.warehouses.Warehouse;
import com.toystorage.backend.models.warehouses.WarehouseLocation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_blances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class InventoryBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Integer quantity;

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
