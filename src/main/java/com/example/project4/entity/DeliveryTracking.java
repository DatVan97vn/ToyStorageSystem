package com.example.project4.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DeliveryTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deliveryId;

    private double latitude;

    private double longitude;

    private LocalDateTime recordedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
// xác định đơn giao
    public String getDeliveryId() { return deliveryId; }
    public void setDeliveryId(String deliveryId) { this.deliveryId = deliveryId; }
// vĩ độ
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
// kinh độ
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
// lưu thời điểm GPS được ghi nhận
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}