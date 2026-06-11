package com.toystorage.backend.services.products;

import com.toystorage.backend.dto.response.products.ProductResponse;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.repository.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private ProductResponse toResponse(Products product) {
        return ProductResponse.builder()
                .id(product.getId())

                .categoryId(
                        product.getCategory() != null
                                ? product.getCategory().getId()
                                : null
                )
                .categoryName(
                        product.getCategory() != null
                                ? product.getCategory().getName()
                                : null
                )

                .supplierId(
                        product.getSupplier() != null
                                ? product.getSupplier().getId()
                                : null
                )
                .supplierName(
                        product.getSupplier() != null
                                ? product.getSupplier().getName()
                                : null
                )

                .name(product.getName())
                .sku(product.getSku())
                .barcode(product.getBarcode())
                .image(product.getImage())
                .unit(product.getUnit())
                .costPrice(product.getCostPrice())
                .salePrice(product.getSalePrice())
                .weight(product.getWeight())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        return toResponse(product);
    }

    public ProductResponse getByBarcode(String barcode) {
        Products product = productRepository.findByBarcode(barcode)
                .orElseThrow(() ->
                        new RuntimeException("Barcode not found")
                );

        return toResponse(product);
    }

    public ProductResponse createProduct(Products product) {
        Products saved = productRepository.save(product);
        return toResponse(saved);
    }

    public ProductResponse updateProduct(Long id, Products request) {

        Products product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        product.setCategory(request.getCategory());
        product.setSupplier(request.getSupplier());
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setBarcode(request.getBarcode());
        product.setImage(request.getImage());
        product.setUnit(request.getUnit());
        product.setCostPrice(request.getCostPrice());
        product.setSalePrice(request.getSalePrice());
        product.setWeight(request.getWeight());
        product.setDescription(request.getDescription());

        Products saved = productRepository.save(product);

        return toResponse(saved);
    }

    public void deleteProduct(Long id) {

        Products product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        productRepository.delete(product);
    }
}