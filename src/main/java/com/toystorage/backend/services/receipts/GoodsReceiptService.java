package com.toystorage.backend.services.receipts;

import com.toystorage.backend.dto.request.receipts.CreateGoodsReceiptItemRequest;
import com.toystorage.backend.dto.request.receipts.CreateGoodsReceiptRequest;
import com.toystorage.backend.dto.request.receipts.ReceiveGoodsReceiptItemRequest;
import com.toystorage.backend.dto.response.receipts.GoodsReceiptItemResponse;
import com.toystorage.backend.dto.response.receipts.GoodsReceiptResponse;
import com.toystorage.backend.enums.inventory.MovementType;
import com.toystorage.backend.enums.receipts.ReceiptStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.inventory.InventoryBalance;
import com.toystorage.backend.models.inventory.StockMovement;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.receipts.GoodsReceipt;
import com.toystorage.backend.models.receipts.GoodsReceiptItem;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.warehouses.WarehouseLocation;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.inventory.InventoryBalanceRepository;
import com.toystorage.backend.repository.inventory.StockMovementRepository;
import com.toystorage.backend.repository.products.ProductRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptItemRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptRepository;
import com.toystorage.backend.repository.transfers.StockTransferRepository;
import com.toystorage.backend.repository.warehouses.WarehouseLocationRepository;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptItemRepository goodsReceiptItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final UserRepository userRepository;
    private final StockTransferRepository stockTransferRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final StockMovementRepository stockMovementRepository;

    private GoodsReceiptResponse toResponse(GoodsReceipt receipt) {
        return GoodsReceiptResponse.builder()
                .id(receipt.getId())
                .receiptCode(receipt.getReceiptCode())
                .manifestId(receipt.getManifestId())
                .transferId(receipt.getTransfer() != null ? receipt.getTransfer().getId() : null)
                .warehouseId(receipt.getWarehouse() != null ? receipt.getWarehouse().getId() : null)
                .warehouseName(receipt.getWarehouse() != null ? receipt.getWarehouse().getName() : null)
                .receivedById(receipt.getReceivedBy() != null ? receipt.getReceivedBy().getId() : null)
                .receivedByName(receipt.getReceivedBy() != null ? receipt.getReceivedBy().getName() : null)
                .checkedById(receipt.getCheckedBy() != null ? receipt.getCheckedBy().getId() : null)
                .checkedByName(receipt.getCheckedBy() != null ? receipt.getCheckedBy().getName() : null)
                .status(receipt.getStatus() != null ? receipt.getStatus().name() : null)
                .note(receipt.getNote())
                .startedReceiveAt(receipt.getStartedReceiveAt())
                .completedReceiveAt(receipt.getCompletedReceiveAt())
                .createdAt(receipt.getCreatedAt())
                .build();
    }

    private GoodsReceiptItemResponse toItemResponse(GoodsReceiptItem item) {
        return GoodsReceiptItemResponse.builder()
                .id(item.getId())
                .goodsReceiptId(item.getGoodsReceipt() != null ? item.getGoodsReceipt().getId() : null)
                .productId(item.getProduct() != null ? item.getProduct().getId() : null)
                .productName(item.getProduct() != null ? item.getProduct().getName() : null)
                .productSku(item.getProduct() != null ? item.getProduct().getSku() : null)
                .productBarcode(item.getProduct() != null ? item.getProduct().getBarcode() : null)
                .expectedQuantity(item.getExpectedQuantity())
                .receivedQuantity(item.getReceivedQuantity())
                .locationId(item.getLocation() != null ? item.getLocation().getId() : null)
                .locationCode(item.getLocation() != null ? item.getLocation().getLocationCode() : null)
                .build();
    }

    private GoodsReceipt findReceiptEntityById(Long id) {
        return goodsReceiptRepository.findById(id)
                .orElseThrow(() -> new BadRequest("GOODS_RECEIPT_NOT_FOUND"));
    }

    @Transactional
    public GoodsReceiptResponse createReceipt(CreateGoodsReceiptRequest request) {
        if (request == null) {
            throw new BadRequest("GOODS_RECEIPT_REQUEST_REQUIRED");
        }

        if (request.getWarehouseId() == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        Warehouses warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));

        StockTransfer transfer = null;

        if (request.getTransferId() != null) {
            transfer = stockTransferRepository.findById(request.getTransferId())
                    .orElseThrow(() -> new BadRequest("TRANSFER_NOT_FOUND"));
        }

        User receivedBy = null;

        if (request.getReceivedById() != null) {
            receivedBy = userRepository.findById(request.getReceivedById())
                    .orElseThrow(() -> new BadRequest("RECEIVER_NOT_FOUND"));
        }

        User checkedBy = null;

        if (request.getCheckedById() != null) {
            checkedBy = userRepository.findById(request.getCheckedById())
                    .orElseThrow(() -> new BadRequest("CHECKER_NOT_FOUND"));
        }

        GoodsReceipt receipt = GoodsReceipt.builder()
                .receiptCode("GR-" + System.currentTimeMillis())
                .manifestId(request.getManifestId())
                .transfer(transfer)
                .warehouse(warehouse)
                .receivedBy(receivedBy)
                .checkedBy(checkedBy)
                .status(ReceiptStatus.DRAFT)
                .note(request.getNote())
                .build();

        GoodsReceipt savedReceipt = goodsReceiptRepository.save(receipt);

        if (request.getItems() != null) {
            for (CreateGoodsReceiptItemRequest itemRequest : request.getItems()) {
                if (itemRequest.getProductId() == null) {
                    throw new BadRequest("PRODUCT_ID_REQUIRED");
                }

                Products product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new BadRequest("PRODUCT_NOT_FOUND"));

                WarehouseLocation location = null;

                if (itemRequest.getLocationId() != null) {
                    location = warehouseLocationRepository.findById(itemRequest.getLocationId())
                            .orElseThrow(() -> new BadRequest("LOCATION_NOT_FOUND"));
                }

                GoodsReceiptItem item = GoodsReceiptItem.builder()
                        .goodsReceipt(savedReceipt)
                        .product(product)
                        .expectedQuantity(itemRequest.getExpectedQuantity())
                        .receivedQuantity(itemRequest.getReceivedQuantity() != null ? itemRequest.getReceivedQuantity() : 0)
                        .location(location)
                        .build();

                goodsReceiptItemRepository.save(item);
            }
        }

        return toResponse(savedReceipt);
    }

    public List<GoodsReceiptResponse> getAllReceipts() {
        return goodsReceiptRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GoodsReceiptResponse getReceiptById(Long id) {
        return toResponse(findReceiptEntityById(id));
    }

    public List<GoodsReceiptResponse> getReceiptsByWarehouse(Long warehouseId) {
        return goodsReceiptRepository.findByWarehouse_Id(warehouseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<GoodsReceiptItemResponse> getReceiptItems(Long receiptId) {
        return goodsReceiptItemRepository.findByGoodsReceipt_Id(receiptId)
                .stream()
                .map(this::toItemResponse)
                .toList();
    }

    @Transactional
    public GoodsReceiptResponse startReceiving(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        receipt.setStatus(ReceiptStatus.RECEIVING);
        receipt.setStartedReceiveAt(LocalDateTime.now());

        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptItemResponse receiveItem(
            Long itemId,
            ReceiveGoodsReceiptItemRequest request
    ) {
        if (request == null) {
            throw new BadRequest("RECEIVE_REQUEST_REQUIRED");
        }

        if (request.getReceivedQuantity() == null) {
            throw new BadRequest("RECEIVED_QUANTITY_REQUIRED");
        }

        if (request.getReceivedQuantity() < 0) {
            throw new BadRequest("RECEIVED_QUANTITY_INVALID");
        }

        GoodsReceiptItem item = goodsReceiptItemRepository.findById(itemId)
                .orElseThrow(() -> new BadRequest("GOODS_RECEIPT_ITEM_NOT_FOUND"));

        item.setReceivedQuantity(request.getReceivedQuantity());

        return toItemResponse(goodsReceiptItemRepository.save(item));
    }

    @Transactional
    public GoodsReceiptResponse completeReceipt(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        if (receipt.getStatus() == ReceiptStatus.COMPLETED) {
            return toResponse(receipt);
        }

        List<GoodsReceiptItem> items = goodsReceiptItemRepository.findByGoodsReceipt_Id(id);

        boolean hasDiscrepancy = false;

        for (GoodsReceiptItem item : items) {
            Integer expectedQuantity = item.getExpectedQuantity() != null ? item.getExpectedQuantity() : 0;
            Integer receivedQuantity = item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0;

            if (!expectedQuantity.equals(receivedQuantity)) {
                hasDiscrepancy = true;
            }

            if (receivedQuantity <= 0) {
                continue;
            }

            Products product = item.getProduct();
            Warehouses warehouse = receipt.getWarehouse();

            InventoryBalance inventoryBalance = inventoryBalanceRepository
                    .findByWarehouse_IdAndProduct_Id(
                            warehouse.getId(),
                            product.getId()
                    )
                    .orElseGet(() ->
                            InventoryBalance.builder()
                                    .warehouse(warehouse)
                                    .product(product)
                                    .quantity(0)
                                    .build()
                    );

            inventoryBalance.setQuantity(
                    inventoryBalance.getQuantity() + receivedQuantity
            );

            inventoryBalanceRepository.save(inventoryBalance);

            StockMovement stockMovement = StockMovement.builder()
                    .warehouse(warehouse)
                    .product(product)
                    .movementType(MovementType.IN)
                    .quantity(receivedQuantity)
                    .referenceType("GOODS_RECEIPT")
                    .referenceId(receipt.getId())
                    .build();

            stockMovementRepository.save(stockMovement);
        }

        receipt.setCompletedReceiveAt(LocalDateTime.now());
        receipt.setStatus(hasDiscrepancy ? ReceiptStatus.DISCREPANCY : ReceiptStatus.COMPLETED);

        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse cancelReceipt(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        receipt.setStatus(ReceiptStatus.CANCELLED);

        return toResponse(goodsReceiptRepository.save(receipt));
    }
}