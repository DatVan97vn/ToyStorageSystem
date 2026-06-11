package com.toystorage.backend.models.warehouses;

import com.toystorage.backend.enums.warehouses.WarehouseType;
import com.toystorage.backend.models.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity

@Table(name = "warehouses")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Warehouses {

    /*
     * Primary Key
     */
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Tên kho
     *
     * Ví dụ:
     * Kho HCM
     * Store Quận 1
     */
    @Column(nullable = false, length = 255)
    private String name;

    /*
     * Mã kho
     *
     * Ví dụ:
     * WH001
     * STORE001
     */
    @Column(nullable = false, unique = true, length = 255)
    private String code;

    /*
     * Loại kho
     *
     * MAIN_WAREHOUSE
     * STORE
     */
    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    private WarehouseType type;

    /*
     * Số điện thoại kho
     */
    private String phone;

    /*
     * Địa chỉ kho
     */
    @Column(columnDefinition = "TEXT")
    private String address;

    /*
     * Người quản lý kho
     *
     * FK -> users.id
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "manager_id")
    private User manager;

    /*
     * Ngày tạo
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /*
     * Ngày cập nhật
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
     * Tự chạy trước khi INSERT
     */
    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();
    }

    /*
     * Tự chạy trước khi UPDATE
     */
    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}