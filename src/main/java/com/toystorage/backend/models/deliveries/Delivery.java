package com.toystorage.backend.models.deliveries;

import com.toystorage.backend.enums.deliveries.DeliveryStatus;
import com.toystorage.backend.models.manifests.ShipmentManifest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_code", unique = true)
    private String deliveryCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manifest_id")
    private ShipmentManifest manifest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = DeliveryStatus.CREATED;
        }
    }
}