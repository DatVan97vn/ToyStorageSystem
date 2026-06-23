package com.toystorage.backend.controllers.deliveries;

import com.toystorage.backend.enums.deliveries.DeliveryStatus;
import com.toystorage.backend.services.deliveries.DeliveryTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery-tracking")
@RequiredArgsConstructor

public class DeliveryTrackingController {

    private final DeliveryTrackingService deliveryTrackingService;

    @PostMapping
    public ResponseEntity<?> createTracking(
            @RequestBody Map<String, Object> payload
    ) {
        Long deliveryId = Long.valueOf(payload.get("deliveryId").toString());
        DeliveryStatus status = DeliveryStatus.valueOf(payload.get("status").toString());
        String note = payload.get("note") != null ? payload.get("note").toString() : null;
        Long updatedById = Long.valueOf(payload.get("updatedById").toString());

        return ResponseEntity.ok(
                deliveryTrackingService.createTracking(
                        deliveryId,
                        status,
                        note,
                        updatedById
                )
        );
    }

    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<?> getTrackingByDelivery(
            @PathVariable Long deliveryId
    ) {
        return ResponseEntity.ok(
                deliveryTrackingService.getTrackingByDelivery(deliveryId)
        );
    }
}