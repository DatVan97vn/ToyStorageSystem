package com.toystorage.backend.services.products;

import com.toystorage.backend.models.products.Product;
import com.toystorage.backend.repository.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service xử lý nghiệp vụ sản phẩm
 */

@Service

@RequiredArgsConstructor

public class ProductService {

    private final ProductRepository productRepository;

    /*
     * Danh sách sản phẩm
     */
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    /*
     * Chi tiết sản phẩm
     */
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );
    }

    /*
     * Tìm theo barcode
     */
    public Product getByBarcode(String barcode) {

        return productRepository.findByBarcode(barcode)
                .orElseThrow(() ->
                        new RuntimeException("Barcode not found")
                );
    }
}