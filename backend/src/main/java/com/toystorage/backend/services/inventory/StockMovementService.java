package com.toystorage.backend.services.inventory;

import com.toystorage.backend.dto.response.inventory.StockMovementResponse;
import com.toystorage.backend.enums.inventory.MovementType;
import com.toystorage.backend.models.inventory.StockMovement;
import com.toystorage.backend.repository.inventory.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.toystorage.backend.models.inventory.InventoryBalance;
import com.toystorage.backend.repository.inventory.InventoryBalanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;

    /*
     * Convert Entity -> DTO
     */
    private StockMovementResponse toResponse(StockMovement movement) {

        return StockMovementResponse.builder()
                .id(movement.getId())

                .warehouseId(
                        movement.getWarehouse() != null
                                ? movement.getWarehouse().getId()
                                : null
                )
                .warehouseName(
                        movement.getWarehouse() != null
                                ? movement.getWarehouse().getName()
                                : null
                )

                .productId(
                        movement.getProduct() != null
                                ? movement.getProduct().getId()
                                : null
                )
                .productName(
                        movement.getProduct() != null
                                ? movement.getProduct().getName()
                                : null
                )

                .movementType(
                        movement.getMovementType() != null
                                ? movement.getMovementType().name()
                                : null
                )
                .quantity(movement.getQuantity())
                .referenceType(movement.getReferenceType())
                .referenceId(movement.getReferenceId())
                .createdAt(movement.getCreatedAt())
                .build();
    }

    public List<StockMovementResponse> getAllMovements() {
        return stockMovementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public StockMovementResponse getMovementById(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Stock movement not found")
                );

        return toResponse(movement);
    }

    public List<StockMovementResponse> getMovementsByWarehouse(Long warehouseId) {
        return stockMovementRepository.findByWarehouse_Id(warehouseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StockMovementResponse> getMovementsByProduct(Long productId) {
        return stockMovementRepository.findByProduct_Id(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<StockMovementResponse> getMovementsByType(MovementType movementType) {
        return stockMovementRepository.findByMovementType(movementType)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public StockMovementResponse createMovement(StockMovement stockMovement) {

        /*
         * 1. Lưu lịch sử movement
         */
        StockMovement saved = stockMovementRepository.save(stockMovement);

        /*
         * 2. Tìm tồn kho hiện tại
         */
        InventoryBalance inventoryBalance =
                inventoryBalanceRepository
                        .findByWarehouse_IdAndProduct_Id(
                                stockMovement.getWarehouse().getId(),
                                stockMovement.getProduct().getId()
                        )
                        .orElse(
                                InventoryBalance.builder()
                                        .warehouse(stockMovement.getWarehouse())
                                        .product(stockMovement.getProduct())
                                        .quantity(0)
                                        .build()
                        );

        /*
         * 3. Update quantity theo movement type
         */
        switch (stockMovement.getMovementType()) {

            case IN, TRANSFER_IN ->

                    inventoryBalance.setQuantity(
                            inventoryBalance.getQuantity()
                                    + stockMovement.getQuantity()
                    );

            case OUT, TRANSFER_OUT ->

                    inventoryBalance.setQuantity(
                            inventoryBalance.getQuantity()
                                    - stockMovement.getQuantity()
                    );

            case ADJUST ->

                    inventoryBalance.setQuantity(
                            stockMovement.getQuantity()
                    );
        }

        /*
         * 4. Save inventory
         */
        inventoryBalanceRepository.save(inventoryBalance);

        /*
         * 5. Return DTO
         */
        return toResponse(saved);
    }

    public void deleteMovement(Long id) {
        StockMovement movement = stockMovementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Stock movement not found")
                );

        stockMovementRepository.delete(movement);
    }
}