package com.toystorage.backend.dto.response.receipts;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptResponse {

    private Long id;

    private String receiptCode;

    private Long supplierId;

    private String supplierName;

    private Long warehouseId;

    private String warehouseName;

    private Long createdById;

    private String createdByName;

    private Long businessStaffId;

    private String businessStaffName;

    private Long receivedById;

    private String receivedByName;

    private Long checkedById;

    private String checkedByName;

    private String status;

    private String note;

    private LocalDateTime requestedAt;

    private LocalDateTime orderedAt;

    private LocalDateTime deliveringAt;

    private LocalDateTime arrivedAt;

    private LocalDateTime startedReceiveAt;

    private LocalDateTime completedReceiveAt;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}