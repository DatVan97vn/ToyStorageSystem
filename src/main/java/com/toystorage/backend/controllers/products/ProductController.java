package com.toystorage.backend.controllers.products;

import com.toystorage.backend.exceptions.BadRequest;
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

        if (id == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        Object product = productService.getProductById(id);

        if (product == null) {
            throw new BadRequest("PRODUCT_NOT_FOUND");
        }

        return ResponseEntity.ok(product);
    }

    /*
     * Scan barcode sản phẩm
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<?> getByBarcode(
            @PathVariable String barcode
    ) {

        if (barcode == null || barcode.isBlank()) {
            throw new BadRequest("BARCODE_REQUIRED");
        }

        Object product = productService.getByBarcode(barcode);

        if (product == null) {
            throw new BadRequest("PRODUCT_NOT_FOUND");
        }

        return ResponseEntity.ok(product);
    }

    /*
     * Tạo sản phẩm
     */
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestBody Products product
    ) {

        if (product == null) {
            throw new BadRequest("PRODUCT_REQUIRED");
        }

        return ResponseEntity.ok(
                productService.createProduct(product)
        );
    }

    /*
     * Cập nhật sản phẩm
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody Products product
    ) {

        if (id == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        if (product == null) {
            throw new BadRequest("PRODUCT_REQUIRED");
        }

        return ResponseEntity.ok(
                productService.updateProduct(id, product)
        );
    }

    /*
     * Xóa sản phẩm
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long id
    ) {

        if (id == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        productService.deleteProduct(id);

        return ResponseEntity.ok(
                "DELETE_PRODUCT_SUCCESS"
        );
    }
}