package com.toystorage.backend.mapper.warehouse;

import com.toystorage.backend.dto.response.warehouses.WarehouseResponse;
import com.toystorage.backend.models.warehouses.Warehouses;

public class WarehouseMapper {

    public static WarehouseResponse toResponse(
            Warehouses warehouse
    ) {

        if (warehouse == null) {
            return null;
        }

        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .code(warehouse.getCode())
                .type(
                        warehouse.getType() != null
                                ? warehouse.getType().name()
                                : null
                )
                .phone(warehouse.getPhone())
                .address(warehouse.getAddress())
                .managerId(
                        warehouse.getManager() != null
                                ? warehouse.getManager().getId()
                                : null
                )
                .managerName(
                        warehouse.getManager() != null
                                ? warehouse.getManager().getName()
                                : null
                )
                .createdAt(warehouse.getCreatedAt())
                .updatedAt(warehouse.getUpdatedAt())
                .build();
    }
}