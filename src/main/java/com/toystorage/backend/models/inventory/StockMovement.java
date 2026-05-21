package com.toystorage.backend.models.inventory;

import com.toystorage.backend.enums.inventory.MovementType;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.products.Product;
import com.toystorage.backend.models.warehouses.Warehouse;
import com.toystorage.backend.models.warehouses.WarehouseLocation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




}
