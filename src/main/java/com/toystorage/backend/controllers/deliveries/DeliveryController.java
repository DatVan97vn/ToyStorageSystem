package com.toystorage.backend.controllers.deliveries;

import com.toystorage.backend.enums.deliveries.DeliveryStatus;
import com.toystorage.backend.services.deliveries.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor

public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<?> createDelivery(
            @RequestBody Map<String, Object> payload
    ) {
        Long manifestId = Long.valueOf(payload.get("manifestId").toString());
        Long driverId = Long.valueOf(payload.get("driverId").toString());

        return ResponseEntity.ok(
                deliveryService.createDelivery(manifestId, driverId)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeliveryById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<?> getDeliveriesByDriver(
            @PathVariable Long driverId
    ) {
        return ResponseEntity.ok(
                deliveryService.getDeliveriesByDriver(driverId)
        );
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @PathVariable DeliveryStatus status
    ) {
        return ResponseEntity.ok(
                deliveryService.updateStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok("DELETE_DELIVERY_SUCCESS");
    }
}