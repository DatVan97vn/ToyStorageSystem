package com.toystorage.backend.dto.response.products;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*
 * Response sản phẩm
 */

@Getter
@Setter
@Builder

public class ProductResponse {

    private Long id;

    private String name;

    private String sku;

    private String barcode;

    private Double salePrice;

    private String image;
}