package com.toystorage.backend.repository.deliveries;

import com.toystorage.backend.models.deliveries.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTrackingRepository
        extends JpaRepository<DeliveryTracking, Long> {

    List<DeliveryTracking> findByDelivery_Id(Long deliveryId);
}