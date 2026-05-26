package com.toystorage.backend.controllers.suppliers;

import com.toystorage.backend.models.suppliers.Suppliers;
import com.toystorage.backend.services.suppliers.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@CrossOrigin("*")
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

        return ResponseEntity.ok(
                supplierService.getSupplierById(id)
        );
    }

    /*
     * Tạo supplier
     */
    @PostMapping
    public ResponseEntity<?> createSupplier(
            @RequestBody Suppliers supplier
    ) {

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

        supplierService.deleteSupplier(id);

        return ResponseEntity.ok(
                "Delete supplier successfully"
        );
    }
}