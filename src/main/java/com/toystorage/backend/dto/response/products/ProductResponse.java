package com.toystorage.backend.dto.response.products;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
 * Response sản phẩm
 */
@Getter
@Setter
@Builder
public class ProductResponse {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private Long supplierId;

    private String supplierName;

    private String name;

    private String sku;

    private String barcode;

    private String image;

    private String unit;

    private BigDecimal costPrice;

    private BigDecimal salePrice;

    private BigDecimal weight;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}