package com.toystorage.backend.services.inventory;

import com.toystorage.backend.dto.request.inventory.PutawayRequest;
import com.toystorage.backend.dto.response.inventory.PutawayTaskResponse;
import com.toystorage.backend.enums.inventory.MovementType;
import com.toystorage.backend.enums.inventory.PutawayStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.inventory.PutawayTaskMapper;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.inventory.InventoryBalance;
import com.toystorage.backend.models.inventory.PutawayTask;
import com.toystorage.backend.models.inventory.StockMovement;
import com.toystorage.backend.models.receipts.GoodsReceipt;
import com.toystorage.backend.models.receipts.GoodsReceiptItem;
import com.toystorage.backend.models.warehouses.WarehouseLocation;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.inventory.InventoryBalanceRepository;
import com.toystorage.backend.repository.inventory.PutawayTaskRepository;
import com.toystorage.backend.repository.inventory.StockMovementRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptItemRepository;
import com.toystorage.backend.repository.warehouses.WarehouseLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PutawayTaskService {

    private final PutawayTaskRepository putawayTaskRepository;
    private final GoodsReceiptItemRepository goodsReceiptItemRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final StockMovementRepository stockMovementRepository;
    private final UserRepository userRepository;
    private final PutawayTaskMapper putawayTaskMapper;

    @Transactional
    public PutawayTaskResponse putaway(PutawayRequest request) {
        if (request == null) {
            throw new BadRequest("PUTAWAY_REQUEST_REQUIRED");
        }

        if (request.getGoodsReceiptItemId() == null) {
            throw new BadRequest("GOODS_RECEIPT_ITEM_ID_REQUIRED");
        }

        if (request.getLocationId() == null) {
            throw new BadRequest("LOCATION_ID_REQUIRED");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequest("PUTAWAY_QUANTITY_INVALID");
        }

        GoodsReceiptItem item = goodsReceiptItemRepository.findById(request.getGoodsReceiptItemId())
                .orElseThrow(() -> new BadRequest("GOODS_RECEIPT_ITEM_NOT_FOUND"));

        GoodsReceipt receipt = item.getGoodsReceipt();

        if (receipt == null) {
            throw new BadRequest("GOODS_RECEIPT_NOT_FOUND");
        }

        WarehouseLocation location = warehouseLocationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new BadRequest("LOCATION_NOT_FOUND"));

        User putawayBy = null;

        if (request.getPutawayById() != null) {
            putawayBy = userRepository.findById(request.getPutawayById())
                    .orElseThrow(() -> new BadRequest("PUTAWAY_USER_NOT_FOUND"));
        }

        int receivedQuantity = item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0;
        int putawayQuantity = item.getPutawayQuantity() != null ? item.getPutawayQuantity() : 0;

        if (putawayQuantity + request.getQuantity() > receivedQuantity) {
            throw new BadRequest("PUTAWAY_QUANTITY_EXCEEDS_RECEIVED");
        }

        PutawayTask task = PutawayTask.builder()
                .goodsReceipt(receipt)
                .goodsReceiptItem(item)
                .warehouse(receipt.getWarehouse())
                .product(item.getProduct())
                .location(location)
                .quantity(request.getQuantity())
                .putawayBy(putawayBy)
                .status(PutawayStatus.COMPLETED)
                .putawayAt(LocalDateTime.now())
                .build();

        PutawayTask savedTask = putawayTaskRepository.save(task);

        InventoryBalance balance = inventoryBalanceRepository
                .findByWarehouse_IdAndLocation_IdAndProduct_Id(
                        receipt.getWarehouse().getId(),
                        location.getId(),
                        item.getProduct().getId()
                )
                .orElseGet(() ->
                        InventoryBalance.builder()
                                .warehouse(receipt.getWarehouse())
                                .location(location)
                                .product(item.getProduct())
                                .quantity(0)
                                .reservedQuantity(0)
                                .build()
                );

        balance.setQuantity(balance.getQuantity() + request.getQuantity());

        inventoryBalanceRepository.save(balance);

        item.setPutawayQuantity(putawayQuantity + request.getQuantity());

        goodsReceiptItemRepository.save(item);

        StockMovement movement = StockMovement.builder()
                .warehouse(receipt.getWarehouse())
                .location(location)
                .product(item.getProduct())
                .movementType(MovementType.IN)
                .quantity(request.getQuantity())
                .referenceType("PUTAWAY_TASK")
                .referenceId(savedTask.getId())
                .createdBy(putawayBy)
                .build();

        stockMovementRepository.save(movement);

        return putawayTaskMapper.toResponse(savedTask);
    }

    public List<PutawayTaskResponse> getAllTasks() {
        return putawayTaskRepository.findAll()
                .stream()
                .map(putawayTaskMapper::toResponse)
                .toList();
    }

    public PutawayTaskResponse getTaskById(Long id) {
        return putawayTaskMapper.toResponse(
                putawayTaskRepository.findById(id)
                        .orElseThrow(() -> new BadRequest("PUTAWAY_TASK_NOT_FOUND"))
        );
    }

    public List<PutawayTaskResponse> getTasksByReceipt(Long receiptId) {
        return putawayTaskRepository.findByGoodsReceipt_Id(receiptId)
                .stream()
                .map(putawayTaskMapper::toResponse)
                .toList();
    }

    public List<PutawayTaskResponse> getTasksByWarehouse(Long warehouseId) {
        return putawayTaskRepository.findByWarehouse_Id(warehouseId)
                .stream()
                .map(putawayTaskMapper::toResponse)
                .toList();
    }
}