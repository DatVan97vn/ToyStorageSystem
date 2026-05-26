package com.toystorage.backend.repository.products;

import com.toystorage.backend.models.products.ProductCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository
        extends JpaRepository<ProductCategories, Long> {

    Optional<ProductCategories> findByCode(String code);
}