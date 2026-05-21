package com.toystorage.backend.repository.products;

import com.toystorage.backend.models.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    Optional<Product> findBySku(String sku);
}