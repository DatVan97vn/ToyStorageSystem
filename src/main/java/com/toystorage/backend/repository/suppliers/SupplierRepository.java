package com.toystorage.backend.repository.suppliers;

import com.toystorage.backend.models.suppliers.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository
        extends JpaRepository<Suppliers, Long> {
}