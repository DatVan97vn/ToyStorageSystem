package com.toystorage.backend.models.suppliers;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity

@Table(name = "suppliers")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Supplier {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Tên nhà cung cấp
     *
     * Ví dụ:
     * Bandai
     * Lego Vietnam
     */
    @Column(nullable = false, length = 255)
    private String name;

    /*
     * Số điện thoại
     */
    @Column(length = 255)
    private String phone;

    /*
     * Email nhà cung cấp
     */
    @Column(length = 255)
    private String email;

    /*
     * Địa chỉ
     */
    @Column(columnDefinition = "TEXT")
    private String address;

    /*
     * Mã số thuế
     */
    @Column(name = "tax_code", length = 255)
    private String taxCode;

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