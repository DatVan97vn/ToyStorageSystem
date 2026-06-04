package com.toystorage.backend.models.dashboard;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey; // Để phân biệt: "CONFIG_MA_THUNG" hoặc "CONFIG_MA_PHIEU"

    @Column(nullable = false, length = 50)
    private String prefix; // Ví dụ: "THG-", "PNK-"

    @Column(name = "current_value", nullable = false)
    private Long currentValue; // Số tự động tăng hiện tại (bắt đầu từ 0)

    @Column(name = "code_length", nullable = false)
    private Integer codeLength; // Độ dài của số đằng sau, ví dụ: 5 (để biến số 1 thành "00001")

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}