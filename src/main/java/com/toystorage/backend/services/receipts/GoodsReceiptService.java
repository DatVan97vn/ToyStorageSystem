package com.toystorage.backend.services.receipts;

import com.toystorage.backend.dto.request.receipts.CreateGoodsReceiptItemRequest;
import com.toystorage.backend.dto.request.receipts.CreateGoodsReceiptRequest;
import com.toystorage.backend.dto.request.receipts.ReceiveGoodsReceiptItemRequest;
import com.toystorage.backend.dto.response.receipts.GoodsReceiptItemResponse;
import com.toystorage.backend.dto.response.receipts.GoodsReceiptResponse;
import com.toystorage.backend.enums.receipts.ReceiptStatus;
import com.toystorage.backend.enums.warehouses.WarehouseType;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.receipts.GoodsReceipt;
import com.toystorage.backend.models.receipts.GoodsReceiptItem;
import com.toystorage.backend.models.suppliers.Suppliers;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.products.ProductRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptItemRepository;
import com.toystorage.backend.repository.receipts.GoodsReceiptRepository;
import com.toystorage.backend.repository.suppliers.SupplierRepository;
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
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;

    private GoodsReceiptResponse toResponse(GoodsReceipt receipt) {
        return GoodsReceiptResponse.builder()
                .id(receipt.getId())
                .receiptCode(receipt.getReceiptCode())

                .supplierId(receipt.getSupplier() != null ? receipt.getSupplier().getId() : null)
                .supplierName(receipt.getSupplier() != null ? receipt.getSupplier().getName() : null)

                .warehouseId(receipt.getWarehouse() != null ? receipt.getWarehouse().getId() : null)
                .warehouseName(receipt.getWarehouse() != null ? receipt.getWarehouse().getName() : null)

                .createdById(receipt.getCreatedBy() != null ? receipt.getCreatedBy().getId() : null)
                .createdByName(receipt.getCreatedBy() != null ? receipt.getCreatedBy().getName() : null)

                .businessStaffId(receipt.getBusinessStaff() != null ? receipt.getBusinessStaff().getId() : null)
                .businessStaffName(receipt.getBusinessStaff() != null ? receipt.getBusinessStaff().getName() : null)

                .receivedById(receipt.getReceivedBy() != null ? receipt.getReceivedBy().getId() : null)
                .receivedByName(receipt.getReceivedBy() != null ? receipt.getReceivedBy().getName() : null)

                .checkedById(receipt.getCheckedBy() != null ? receipt.getCheckedBy().getId() : null)
                .checkedByName(receipt.getCheckedBy() != null ? receipt.getCheckedBy().getName() : null)

                .status(receipt.getStatus() != null ? receipt.getStatus().name() : null)
                .note(receipt.getNote())

                .requestedAt(receipt.getRequestedAt())
                .orderedAt(receipt.getOrderedAt())
                .deliveringAt(receipt.getDeliveringAt())
                .arrivedAt(receipt.getArrivedAt())
                .startedReceiveAt(receipt.getStartedReceiveAt())
                .completedReceiveAt(receipt.getCompletedReceiveAt())
                .completedAt(receipt.getCompletedAt())
                .createdAt(receipt.getCreatedAt())
                .updatedAt(receipt.getUpdatedAt())
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
                .putawayQuantity(item.getPutawayQuantity())
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

        if (request.getSupplierId() == null) {
            throw new BadRequest("SUPPLIER_ID_REQUIRED");
        }

        Warehouses warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));

        if (warehouse.getType() != WarehouseType.MAIN_WAREHOUSE) {
            throw new BadRequest("ONLY_MAIN_WAREHOUSE_CAN_RECEIVE_FROM_SUPPLIER");
        }

        Suppliers supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new BadRequest("SUPPLIER_NOT_FOUND"));

        User createdBy = null;
        if (request.getCreatedById() != null) {
            createdBy = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new BadRequest("CREATOR_NOT_FOUND"));
        }

        User businessStaff = null;
        if (request.getBusinessStaffId() != null) {
            businessStaff = userRepository.findById(request.getBusinessStaffId())
                    .orElseThrow(() -> new BadRequest("BUSINESS_STAFF_NOT_FOUND"));
        }

        GoodsReceipt receipt = GoodsReceipt.builder()
                .receiptCode("GR-" + System.currentTimeMillis())
                .supplier(supplier)
                .warehouse(warehouse)
                .createdBy(createdBy)
                .businessStaff(businessStaff)
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

                GoodsReceiptItem item = GoodsReceiptItem.builder()
                        .goodsReceipt(savedReceipt)
                        .product(product)
                        .expectedQuantity(itemRequest.getExpectedQuantity() != null ? itemRequest.getExpectedQuantity() : 0)
                        .receivedQuantity(0)
                        .putawayQuantity(0)
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
    public GoodsReceiptResponse markRequested(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);
        receipt.setStatus(ReceiptStatus.REQUESTED);
        receipt.setRequestedAt(LocalDateTime.now());
        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse markOrdering(Long id, Long businessStaffId) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        User businessStaff = userRepository.findById(businessStaffId)
                .orElseThrow(() -> new BadRequest("BUSINESS_STAFF_NOT_FOUND"));

        receipt.setBusinessStaff(businessStaff);
        receipt.setStatus(ReceiptStatus.ORDERING);
        receipt.setOrderedAt(LocalDateTime.now());

        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse markDelivering(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);
        receipt.setStatus(ReceiptStatus.DELIVERING);
        receipt.setDeliveringAt(LocalDateTime.now());
        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse markArrived(Long id, Long receivedById) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        User receivedBy = userRepository.findById(receivedById)
                .orElseThrow(() -> new BadRequest("RECEIVER_NOT_FOUND"));

        receipt.setReceivedBy(receivedBy);
        receipt.setStatus(ReceiptStatus.ARRIVED);
        receipt.setArrivedAt(LocalDateTime.now());

        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse startChecking(Long id, Long checkedById) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        User checkedBy = userRepository.findById(checkedById)
                .orElseThrow(() -> new BadRequest("CHECKER_NOT_FOUND"));

        receipt.setCheckedBy(checkedBy);
        receipt.setStatus(ReceiptStatus.CHECKING);
        receipt.setStartedReceiveAt(LocalDateTime.now());

        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptItemResponse receiveItem(Long itemId, ReceiveGoodsReceiptItemRequest request) {
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
    public GoodsReceiptResponse completeChecking(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        List<GoodsReceiptItem> items = goodsReceiptItemRepository.findByGoodsReceipt_Id(id);

        boolean hasDiscrepancy = false;

        for (GoodsReceiptItem item : items) {
            int expected = item.getExpectedQuantity() != null ? item.getExpectedQuantity() : 0;
            int received = item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0;

            if (expected != received) {
                hasDiscrepancy = true;
            }
        }

        receipt.setCompletedReceiveAt(LocalDateTime.now());
        receipt.setStatus(hasDiscrepancy ? ReceiptStatus.DISCREPANCY : ReceiptStatus.CHECKED);

        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse moveToPutaway(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);
        receipt.setStatus(ReceiptStatus.PUTAWAY);
        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse completeReceipt(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);

        List<GoodsReceiptItem> items = goodsReceiptItemRepository.findByGoodsReceipt_Id(id);

        boolean allPutaway = items.stream().allMatch(item -> {
            int received = item.getReceivedQuantity() != null ? item.getReceivedQuantity() : 0;
            int putaway = item.getPutawayQuantity() != null ? item.getPutawayQuantity() : 0;
            return received > 0 && putaway >= received;
        });

        if (!allPutaway) {
            throw new BadRequest("PUTAWAY_NOT_COMPLETED");
        }

        receipt.setStatus(ReceiptStatus.COMPLETED);
        receipt.setCompletedAt(LocalDateTime.now());

        return toResponse(goodsReceiptRepository.save(receipt));
    }

    @Transactional
    public GoodsReceiptResponse cancelReceipt(Long id) {
        GoodsReceipt receipt = findReceiptEntityById(id);
        receipt.setStatus(ReceiptStatus.CANCELLED);
        return toResponse(goodsReceiptRepository.save(receipt));
    }
}