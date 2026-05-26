package com.toystorage.backend.controllers.products;

import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.services.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController

@RequestMapping("/api/products")

@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /*
     * Danh sách sản phẩm
     */
    @GetMapping

    public ResponseEntity<?> getAllProducts() {

        return ResponseEntity.ok(
                productService.getAllProducts()
        );
    }

    /*
     * Chi tiết sản phẩm
     */
    @GetMapping("/{id}")

    public ResponseEntity<?> getProductById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }

    /*
     * Scan barcode sản phẩm
     */
    @GetMapping("/barcode/{barcode}")

    public ResponseEntity<?> getByBarcode(
            @PathVariable String barcode
    ) {

        return ResponseEntity.ok(
                productService.getByBarcode(barcode)
        );
    }
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestBody Products product
    ) {

        return ResponseEntity.ok(
                productService.createProduct(product)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody Products product
    ) {

        return ResponseEntity.ok(
                productService.updateProduct(id, product)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteProduct(id);

        return ResponseEntity.ok("Delete product successfully");
    }
}