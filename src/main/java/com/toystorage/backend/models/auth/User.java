package com.toystorage.backend.models.auth;

import com.toystorage.backend.enums.users.UserStatus;
import com.toystorage.backend.models.warehouses.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
 * Entity User
 *
 * Mapping với table users trong database
 */
@Entity

@Table(name = "users")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    /*
     * Primary Key
     */
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Role của user
     *
     * FK -> roles.id
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "role_id")
    private Role role;

    /*
     * Kho làm việc của user
     *
     * FK -> warehouses.id
     *
     * Có thể null:
     * - IT
     * - Sales
     * - Admin
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    /*
     * Tên nhân viên
     */
    @Column(nullable = false, length = 255)
    private String name;

    /*
     * Email đăng nhập
     */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /*
     * Password đã hash BCrypt
     */
    @Column(nullable = false)
    private String password;

    /*
     * Số điện thoại
     */
    @Column(unique = true)
    private String phone;

    /*
     * CCCD / CMND
     */
    @Column(name = "citizen_id", unique = true)
    private String citizenId;

    /*
     * Bật/tắt xác thực 2 lớp
     *
     * true  = bật
     * false = tắt
     */
    @Column(name = "two_factor_enabled")
    private Boolean twoFactorEnabled = false;

    /*
     * Secret key cho Google Authenticator
     */
    @Column(name = "two_factor_secret")
    private String twoFactorSecret;

    /*
     * Đăng nhập lần đầu
     *
     * true  = bắt đổi password
     * false = đã đổi password
     */
    @Column(name = "first_login")
    private Boolean firstLogin = true;

    /*
     * Trạng thái user
     *
     * ACTIVE
     * INACTIVE
     * LOCKED
     */
    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    private UserStatus status;

    /*
     * Thời gian login gần nhất
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

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
     * Hàm tự chạy trước khi INSERT
     */
    @PrePersist
    public void prePersist() {

        this.createdAt = LocalDateTime.now();

        this.updatedAt = LocalDateTime.now();

        /*
         * Nếu chưa set status
         * => mặc định ACTIVE
         */
        if (this.status == null) {

            this.status = UserStatus.ACTIVE;
        }

        /*
         * Nếu chưa set 2FA
         * => mặc định false
         */
        if (this.twoFactorEnabled == null) {

            this.twoFactorEnabled = false;
        }

        /*
         * Nếu chưa set first login
         * => mặc định true
         */
        if (this.firstLogin == null) {

            this.firstLogin = true;
        }
    }

    /*
     * Hàm tự chạy trước khi UPDATE
     */
    @PreUpdate
    public void preUpdate() {

        this.updatedAt = LocalDateTime.now();
    }
}