package com.example.project4.service;

import com.example.project4.entity.DeliveryTracking;
import com.example.project4.repository.DeliveryTrackingRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Service
public class DeliveryTrackingService {
    @Autowired
    private DeliveryTrackingRepository repository;
    //Lưu vị trí
    public DeliveryTracking save(DeliveryTracking deliveryTracking) {
        deliveryTracking.setRecordedAt(LocalDateTime.now());
        return repository.save(deliveryTracking);
    }
    //Lấy vị trị mới nhất
    public DeliveryTracking getLastest(String deliveryId){
        return repository.findTopByDeliveryIdOrderByRecordedAtDesc(deliveryId);
    }

}
