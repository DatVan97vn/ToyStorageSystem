package com.toystorage.backend.repository.products;

import com.toystorage.backend.models.products.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Products, Long> {

    Optional<Products> findByBarcode(String barcode);

    Optional<Products> findBySku(String sku);
}