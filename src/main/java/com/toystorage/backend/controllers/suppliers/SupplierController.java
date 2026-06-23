package com.toystorage.backend.controllers.suppliers;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.suppliers.Suppliers;
import com.toystorage.backend.services.suppliers.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor

public class SupplierController {

    private final SupplierService supplierService;

    /*
     * Danh sách supplier
     */
    @GetMapping
    public ResponseEntity<?> getAllSuppliers() {

        return ResponseEntity.ok(
                supplierService.getAllSuppliers()
        );
    }

    /*
     * Chi tiết supplier
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(
            @PathVariable Long id
    ) {

        if (id == null) {
            throw new BadRequest("SUPPLIER_ID_REQUIRED");
        }

        Object supplier = supplierService.getSupplierById(id);

        if (supplier == null) {
            throw new BadRequest("SUPPLIER_NOT_FOUND");
        }

        return ResponseEntity.ok(supplier);
    }

    /*
     * Tạo supplier
     */
    @PostMapping
    public ResponseEntity<?> createSupplier(
            @RequestBody Suppliers supplier
    ) {

        if (supplier == null) {
            throw new BadRequest("SUPPLIER_REQUIRED");
        }

        return ResponseEntity.ok(
                supplierService.createSupplier(supplier)
        );
    }

    /*
     * Cập nhật supplier
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(
            @PathVariable Long id,
            @RequestBody Suppliers supplier
    ) {

        if (id == null) {
            throw new BadRequest("SUPPLIER_ID_REQUIRED");
        }

        if (supplier == null) {
            throw new BadRequest("SUPPLIER_REQUIRED");
        }

        return ResponseEntity.ok(
                supplierService.updateSupplier(id, supplier)
        );
    }

    /*
     * Xóa supplier
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(
            @PathVariable Long id
    ) {

        if (id == null) {
            throw new BadRequest("SUPPLIER_ID_REQUIRED");
        }

        supplierService.deleteSupplier(id);

        return ResponseEntity.ok(
                "DELETE_SUPPLIER_SUCCESS"
        );
    }
}