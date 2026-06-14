package com.toystorage.backend.services.deliveries;

import com.toystorage.backend.enums.deliveries.DeliveryStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.deliveries.Delivery;
import com.toystorage.backend.models.deliveries.DeliveryTracking;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.deliveries.DeliveryRepository;
import com.toystorage.backend.repository.deliveries.DeliveryTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryTrackingService {

    private final DeliveryTrackingRepository deliveryTrackingRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;

    public DeliveryTracking createTracking(
            Long deliveryId,
            DeliveryStatus status,
            String note,
            Long updatedById
    ) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new BadRequest("DELIVERY_NOT_FOUND"));

        User user = userRepository.findById(updatedById)
                .orElseThrow(() -> new BadRequest("USER_NOT_FOUND"));

        DeliveryTracking tracking = DeliveryTracking.builder()
                .delivery(delivery)
                .status(status)
                .note(note)
                .updatedBy(user)
                .build();

        return deliveryTrackingRepository.save(tracking);
    }

    public List<DeliveryTracking> getTrackingByDelivery(Long deliveryId) {
        return deliveryTrackingRepository.findByDelivery_Id(deliveryId);
    }
}