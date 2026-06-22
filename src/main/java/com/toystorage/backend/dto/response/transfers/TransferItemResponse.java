package com.toystorage.backend.dto.response.transfers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferItemResponse {

    private Long id;

    private Long productId;
    private String productName;

    private Integer requestedQuantity;
    private Integer pickedQuantity;
    private Integer receivedQuantity;
}