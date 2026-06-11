package com.toystorage.backend.models.dashboard;

import com.toystorage.backend.models.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_sessions")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class WorkSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Nhân viên
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "user_id")
    private User user;

    /*
     * STOCK_TRANSFER
     */
    @Column(name = "reference_type")
    private String referenceType;

    /*
     * ID tham chiếu
     */
    @Column(name = "reference_id")
    private Long referenceId;

    /*
     * Bắt đầu
     */
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    /*
     * Kết thúc
     */
    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    /*
     * Tổng thời gian (phút)
     */
    private Integer duration;
}