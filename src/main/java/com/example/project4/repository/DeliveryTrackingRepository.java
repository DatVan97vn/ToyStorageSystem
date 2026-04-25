package com.example.project4.repository;

import com.example.project4.entity.DeliveryTracking;
import org.springframework.data.jpa.repository.JpaRepository;
//lấy định vị mới nhất
public interface DeliveryTrackingRepository extends JpaRepository<DeliveryTracking, Long> {

    DeliveryTracking findTopByDeliveryIdOrderByRecordedAtDesc(String deliveryId);
}