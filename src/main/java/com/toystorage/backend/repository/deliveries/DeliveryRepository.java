package com.toystorage.backend.repository.deliveries;

import com.toystorage.backend.models.deliveries.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByDeliveryCode(String deliveryCode);

    List<Delivery> findByDriver_Id(Long driverId);

    List<Delivery> findByManifest_Id(Long manifestId);
}