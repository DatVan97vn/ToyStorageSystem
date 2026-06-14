package com.toystorage.backend.services.deliveries;

import com.toystorage.backend.enums.deliveries.DeliveryStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.deliveries.Delivery;
import com.toystorage.backend.models.deliveries.Driver;
import com.toystorage.backend.models.manifests.ShipmentManifest;
import com.toystorage.backend.repository.deliveries.DeliveryRepository;
import com.toystorage.backend.repository.deliveries.DriverRepository;
import com.toystorage.backend.repository.manifests.ShipmentManifestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final ShipmentManifestRepository shipmentManifestRepository;

    public Delivery createDelivery(Long manifestId, Long driverId) {
        if (manifestId == null) {
            throw new BadRequest("MANIFEST_ID_REQUIRED");
        }

        if (driverId == null) {
            throw new BadRequest("DRIVER_ID_REQUIRED");
        }

        ShipmentManifest manifest = shipmentManifestRepository.findById(manifestId)
                .orElseThrow(() -> new BadRequest("MANIFEST_NOT_FOUND"));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new BadRequest("DRIVER_NOT_FOUND"));

        Delivery delivery = Delivery.builder()
                .deliveryCode("DL-" + System.currentTimeMillis())
                .manifest(manifest)
                .driver(driver)
                .status(DeliveryStatus.CREATED)
                .build();

        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new BadRequest("DELIVERY_NOT_FOUND"));
    }

    public List<Delivery> getDeliveriesByDriver(Long driverId) {
        return deliveryRepository.findByDriver_Id(driverId);
    }

    public Delivery updateStatus(Long id, DeliveryStatus status) {
        Delivery delivery = getDeliveryById(id);

        delivery.setStatus(status);

        if (status == DeliveryStatus.DELIVERING) {
            delivery.setDepartureTime(LocalDateTime.now());
        }

        if (
                status == DeliveryStatus.ARRIVED
                        || status == DeliveryStatus.RECEIVED
                        || status == DeliveryStatus.COMPLETED
        ) {
            delivery.setArrivalTime(LocalDateTime.now());
        }

        return deliveryRepository.save(delivery);
    }

    public void deleteDelivery(Long id) {
        Delivery delivery = getDeliveryById(id);
        deliveryRepository.delete(delivery);
    }
}