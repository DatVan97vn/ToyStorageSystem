package com.toystorage.backend.services.warehouses;

import com.toystorage.backend.dto.response.warehouses.WarehouseResponse;
import com.toystorage.backend.exceptions.NotFound;
import com.toystorage.backend.mapper.warehouse.WarehouseMapper;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public List<WarehouseResponse> getAllWarehouses() {

        return warehouseRepository.findAll()
                .stream()
                .map(WarehouseMapper::toResponse)
                .toList();
    }

    public WarehouseResponse getWarehouseById(Long id) {

        Warehouses warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new NotFound(
                                "Warehouse not found"
                        )
                );

        return WarehouseMapper.toResponse(warehouse);
    }

    public WarehouseResponse getWarehouseByCode(String code) {

        Warehouses warehouse = warehouseRepository.findByCode(code)
                .orElseThrow(() ->
                        new NotFound(
                                "Warehouse code not found"
                        )
                );

        return WarehouseMapper.toResponse(warehouse);
    }

    public WarehouseResponse createWarehouse(
            Warehouses warehouse
    ) {

        Warehouses saved = warehouseRepository.save(warehouse);

        return WarehouseMapper.toResponse(saved);
    }

    public WarehouseResponse updateWarehouse(
            Long id,
            Warehouses request
    ) {

        Warehouses warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new NotFound(
                                "Warehouse not found"
                        )
                );

        warehouse.setName(request.getName());
        warehouse.setCode(request.getCode());
        warehouse.setType(request.getType());
        warehouse.setPhone(request.getPhone());
        warehouse.setAddress(request.getAddress());
        warehouse.setManager(request.getManager());

        Warehouses saved = warehouseRepository.save(warehouse);

        return WarehouseMapper.toResponse(saved);
    }

    public void deleteWarehouse(Long id) {

        Warehouses warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new NotFound(
                                "Warehouse not found"
                        )
                );

        warehouseRepository.delete(warehouse);
    }
}