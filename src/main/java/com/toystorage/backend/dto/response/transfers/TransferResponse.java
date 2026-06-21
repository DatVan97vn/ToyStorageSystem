package com.toystorage.backend.dto.response.transfers;

import com.toystorage.backend.enums.transfers.TransferStatus;
import com.toystorage.backend.dto.response.warehouses.WarehouseResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TransferResponse {

    private Long id;

    private String transferCode;

    private WarehouseResponse fromWarehouse;

    private WarehouseResponse toWarehouse;

    private TransferStatus status;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    private LocalDateTime completedAt;

    private List<TransferItemResponse> items;
}