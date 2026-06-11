package com.toystorage.backend.services.warehouses;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.warehouses.WarehouseLocation;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.warehouses.WarehouseLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseLocationService {

    private final WarehouseLocationRepository warehouseLocationRepository;

    public List<WarehouseLocation> getLocationsByWarehouse(Long warehouseId) {
        return warehouseLocationRepository.findByWarehouseId(warehouseId);
    }

    public WarehouseLocation createLocation(
            Warehouses warehouse,
            String zone,
            String aisle,
            String shelf,
            String level,
            String bin
    ) {
        if (warehouse == null || warehouse.getId() == null) {
            throw new BadRequest("WAREHOUSE_REQUIRED");
        }

        String locationCode = zone + "-" + aisle + "-" + shelf + "-" + level + "-" + bin;

        if (warehouseLocationRepository.existsByLocationCode(locationCode)) {
            throw new BadRequest("LOCATION_CODE_EXISTS");
        }

        WarehouseLocation location = new WarehouseLocation();
        location.setWarehouse(warehouse);
        location.setZone(zone);
        location.setAisle(aisle);
        location.setShelf(shelf);
        location.setLevelCode(level);
        location.setBinCode(bin);
        location.setLocationCode(locationCode);

        return warehouseLocationRepository.save(location);
    }
}