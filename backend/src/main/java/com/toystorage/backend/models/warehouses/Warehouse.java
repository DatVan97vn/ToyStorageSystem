package com.toystorage.backend.models.warehouses;

import com.toystorage.backend.enums.warehouses.WarehouseType;
import com.toystorage.backend.models.auth.User;
import jakarta.persistence.*;
import lambok.*;

import java.time.LocalDateTime;

@Entity

@Table(name = "warehouses")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String code;

    @Enumrated(EnumType.STRING)
    @Column(nullable = false)
    private WarehousesType type;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }



}
