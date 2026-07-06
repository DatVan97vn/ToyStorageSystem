package com.toystorage.backend.dto.response.inventory;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutawayTaskResponse {

    private Long id;

    private Long goodsReceiptId;

    private Long goodsReceiptItemId;

    private Long warehouseId;

    private String warehouseName;

    private Long productId;

    private String productName;

    private String productSku;

    private Long locationId;

    private String locationCode;

    private String locationBarcode;

    private Integer quantity;

    private Long putawayById;

    private String putawayByName;

    private String status;

    private LocalDateTime putawayAt;

    private LocalDateTime createdAt;
}