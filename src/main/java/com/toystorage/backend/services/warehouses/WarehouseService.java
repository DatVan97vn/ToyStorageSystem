package com.toystorage.backend.services.warehouses;

import com.toystorage.backend.dto.response.warehouses.WarehouseResponse;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public List<Warehouses> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public WarehouseResponse getWarehouseById(Long id) {
        Warehouses warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));

        WarehouseResponse response = new WarehouseResponse();
        response.setId(warehouse.getId());
        response.setCode(warehouse.getCode());
        response.setName(warehouse.getName());

        return response;
    }

    public Warehouses getWarehouseByCode(String code) {
        return warehouseRepository.findByCode(code)
                .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));
    }

    public Warehouses createWarehouse(Warehouses warehouse) {
        if (warehouse.getCode() == null || warehouse.getCode().isBlank()) {
            throw new BadRequest("WAREHOUSE_CODE_REQUIRED");
        }

        if (warehouse.getName() == null || warehouse.getName().isBlank()) {
            throw new BadRequest("WAREHOUSE_NAME_REQUIRED");
        }

        if (warehouseRepository.existsByCode(warehouse.getCode())) {
            throw new BadRequest("WAREHOUSE_CODE_EXISTS");
        }

        warehouse.setCode(warehouse.getCode().trim());
        warehouse.setName(warehouse.getName().trim());

        return warehouseRepository.save(warehouse);
    }

    public Warehouses updateWarehouse(Long id, Warehouses warehouse) {
        Warehouses oldWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));

        if (warehouse.getCode() != null && !warehouse.getCode().isBlank()) {
            oldWarehouse.setCode(warehouse.getCode().trim());
        }

        if (warehouse.getName() != null && !warehouse.getName().isBlank()) {
            oldWarehouse.setName(warehouse.getName().trim());
        }

        return warehouseRepository.save(oldWarehouse);
    }

    public void deleteWarehouse(Long id) {
        Warehouses warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));

        warehouseRepository.delete(warehouse);
    }
}