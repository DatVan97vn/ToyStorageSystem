package com.toystorage.backend.repository.inventory;

import com.toystorage.backend.enums.inventory.PutawayStatus;
import com.toystorage.backend.models.inventory.PutawayTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PutawayTaskRepository extends JpaRepository<PutawayTask, Long> {

    List<PutawayTask> findByGoodsReceipt_Id(Long goodsReceiptId);

    List<PutawayTask> findByGoodsReceiptItem_Id(Long goodsReceiptItemId);

    List<PutawayTask> findByWarehouse_Id(Long warehouseId);

    List<PutawayTask> findByLocation_Id(Long locationId);

    List<PutawayTask> findByStatus(PutawayStatus status);
}