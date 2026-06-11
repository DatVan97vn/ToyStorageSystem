package com.toystorage.backend.services.suppliers;

import com.toystorage.backend.dto.response.suppliers.SupplierResponse;
import com.toystorage.backend.models.suppliers.Suppliers;
import com.toystorage.backend.repository.suppliers.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    private SupplierResponse toResponse(Suppliers supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .taxCode(supplier.getTaxCode())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SupplierResponse getSupplierById(Long id) {
        Suppliers supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Supplier not found with id: " + id));

        return toResponse(supplier);
    }

    public SupplierResponse createSupplier(Suppliers supplier) {
        Suppliers saved = supplierRepository.save(supplier);
        return toResponse(saved);
    }

    public SupplierResponse updateSupplier(Long id, Suppliers request) {

        Suppliers supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Supplier not found with id: " + id));

        supplier.setName(request.getName());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setTaxCode(request.getTaxCode());

        Suppliers saved = supplierRepository.save(supplier);

        return toResponse(saved);
    }

    public void deleteSupplier(Long id) {

        Suppliers supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }
}