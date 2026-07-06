package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.enums.inventory.ReplenishmentStatus;
import com.toystorage.backend.models.inventory.ReplenishmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplenishmentRequestRepository extends JpaRepository<ReplenishmentRequest, Long> {

    List<ReplenishmentRequest> findByRequestWarehouse_Id(Long warehouseId);

    List<ReplenishmentRequest> findBySourceWarehouse_Id(Long warehouseId);

    List<ReplenishmentRequest> findByProduct_Id(Long productId);

    List<ReplenishmentRequest> findByStatus(ReplenishmentStatus status);
}