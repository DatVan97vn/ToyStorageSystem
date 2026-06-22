package com.toystorage.backend.services.transfers;

import com.toystorage.backend.dto.request.transfers.CreateTransferRequest;
import com.toystorage.backend.dto.request.transfers.CreateTransferItemRequest;
import com.toystorage.backend.dto.response.transfers.TransferResponse;
import com.toystorage.backend.enums.transfers.TransferStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.transfers.TransferMapper;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.transfers.StockTransferItems;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.products.ProductRepository;
import com.toystorage.backend.repository.transfers.StockTransferItemRepository;
import com.toystorage.backend.repository.transfers.StockTransferRepository;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final StockTransferItemRepository stockTransferItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productsRepository;

    @Transactional
    public TransferResponse createTransfer(
            CreateTransferRequest request
    ) {
        if (request == null) {
            throw new BadRequest("TRANSFER_REQUEST_REQUIRED");
        }

        if (request.getFromWarehouseId() == null) {
            throw new BadRequest("FROM_WAREHOUSE_ID_REQUIRED");
        }

        if (request.getToWarehouseId() == null) {
            throw new BadRequest("TO_WAREHOUSE_ID_REQUIRED");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequest("TRANSFER_ITEMS_REQUIRED");
        }

        Warehouses fromWarehouse =
                warehouseRepository.findById(request.getFromWarehouseId())
                        .orElseThrow(() ->
                                new BadRequest("FROM_WAREHOUSE_NOT_FOUND")
                        );

        Warehouses toWarehouse =
                warehouseRepository.findById(request.getToWarehouseId())
                        .orElseThrow(() ->
                                new BadRequest("TO_WAREHOUSE_NOT_FOUND")
                        );

        StockTransfer transfer =
                StockTransfer.builder()
                        .transferCode("TRF-" + System.currentTimeMillis())
                        .fromWarehouse(fromWarehouse)
                        .toWarehouse(toWarehouse)
                        .status(TransferStatus.DRAFT)
                        .createdAt(LocalDateTime.now())
                        .build();

        StockTransfer savedTransfer =
                stockTransferRepository.save(transfer);

        for (CreateTransferItemRequest itemRequest : request.getItems()) {
            if (itemRequest.getProductId() == null) {
                throw new BadRequest("PRODUCT_ID_REQUIRED");
            }

            if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
                throw new BadRequest("QUANTITY_INVALID");
            }

            Products product =
                    productsRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() ->
                                    new BadRequest("PRODUCT_NOT_FOUND")
                            );

            StockTransferItems item =
                    StockTransferItems.builder()
                            .transfer(savedTransfer)
                            .product(product)
                            .requestedQuantity(itemRequest.getQuantity())
                            .pickedQuantity(0)
                            .receivedQuantity(0)
                            .build();

            stockTransferItemRepository.save(item);
        }

        return getTransferById(savedTransfer.getId());
    }

    @Transactional(readOnly = true)
    public List<TransferResponse> getAllTransfers() {
        return stockTransferRepository.findAllWithWarehouses()
                .stream()
                .map(transfer -> {
                    List<StockTransferItems> items =
                            stockTransferItemRepository.findByTransfer_Id(
                                    transfer.getId()
                            );

                    return TransferMapper.toResponse(transfer, items);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public TransferResponse getTransferById(Long id) {
        if (id == null) {
            throw new BadRequest("TRANSFER_ID_REQUIRED");
        }

        StockTransfer transfer =
                stockTransferRepository.findDetailById(id)
                        .orElseThrow(() ->
                                new BadRequest("TRANSFER_NOT_FOUND")
                        );

        List<StockTransferItems> items =
                stockTransferItemRepository.findByTransfer_Id(id);

        return TransferMapper.toResponse(transfer, items);
    }

    @Transactional
    public TransferResponse approveTransfer(Long id) {
        StockTransfer transfer =
                stockTransferRepository.findDetailById(id)
                        .orElseThrow(() ->
                                new BadRequest("TRANSFER_NOT_FOUND")
                        );

        transfer.setStatus(TransferStatus.APPROVED);
        transfer.setApprovedAt(LocalDateTime.now());

        StockTransfer saved =
                stockTransferRepository.save(transfer);

        List<StockTransferItems> items =
                stockTransferItemRepository.findByTransfer_Id(id);

        return TransferMapper.toResponse(saved, items);
    }

    @Transactional
    public TransferResponse completeTransfer(Long id) {
        StockTransfer transfer =
                stockTransferRepository.findDetailById(id)
                        .orElseThrow(() ->
                                new BadRequest("TRANSFER_NOT_FOUND")
                        );

        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setCompletedAt(LocalDateTime.now());

        StockTransfer saved =
                stockTransferRepository.save(transfer);

        List<StockTransferItems> items =
                stockTransferItemRepository.findByTransfer_Id(id);

        return TransferMapper.toResponse(saved, items);
    }
}