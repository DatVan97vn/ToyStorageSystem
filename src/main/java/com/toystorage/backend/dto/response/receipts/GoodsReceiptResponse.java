package com.toystorage.backend.dto.response.receipts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/*
 * Response phiếu nhận hàng
 */
@Getter
@Setter
@Builder
public class GoodsReceiptResponse {

    private Long id;

    private String receiptCode;

    private Long manifestId;

    private Long transferId;

    private Long warehouseId;

    private String warehouseName;

    private Long receivedById;

    private String receivedByName;

    private Long checkedById;

    private String checkedByName;

    private String status;

    private String note;

    private LocalDateTime startedReceiveAt;

    private LocalDateTime completedReceiveAt;

    private LocalDateTime createdAt;
}