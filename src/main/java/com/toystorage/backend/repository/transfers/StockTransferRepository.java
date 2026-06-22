package com.toystorage.backend.repository.transfers;

import com.toystorage.backend.models.transfers.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockTransferRepository
        extends JpaRepository<StockTransfer, Long> {

    Optional<StockTransfer> findByTransferCode(String transferCode);

    @Query("""
        SELECT DISTINCT t
        FROM StockTransfer t
        LEFT JOIN FETCH t.fromWarehouse
        LEFT JOIN FETCH t.toWarehouse
    """)
    List<StockTransfer> findAllWithWarehouses();

    @Query("""
        SELECT t
        FROM StockTransfer t
        LEFT JOIN FETCH t.fromWarehouse
        LEFT JOIN FETCH t.toWarehouse
        WHERE t.id = :id
    """)
    Optional<StockTransfer> findDetailById(Long id);
}