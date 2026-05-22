package com.toystorage.backend.services.warehouses;

import com.toystorage.backend.models.warehouses.Warehouse;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service kho
 */

@Service

@RequiredArgsConstructor

public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    /*
     * Danh sách kho
     */
    public List<Warehouse> getAllWarehouses() {

        return warehouseRepository.findAll();
    }

    /*
     * Chi tiết kho
     */
    public Warehouse getWarehouseById(Long id) {

        return warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Warehouse not found")
                );
    }
}