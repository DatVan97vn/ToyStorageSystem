package com.toystorage.backend.services.transfers;

import com.toystorage.backend.dto.request.transfers.CreateTransferRequest;
import com.toystorage.backend.dto.response.transfers.TransferResponse;
import com.toystorage.backend.enums.transfers.TransferStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.transfers.TransferMapper;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.transfers.StockTransferRepository;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockTransferService {

    private final StockTransferRepository stockTransferRepository;
    private final WarehouseRepository warehousesRepository;

    public StockTransfer createTransfer(CreateTransferRequest request) {

        Warehouses fromWarehouse = warehousesRepository.findById(request.getFromWarehouseId())
                .orElseThrow(() -> new BadRequest("FROM_WAREHOUSE_NOT_FOUND"));

        Warehouses toWarehouse = warehousesRepository.findById(request.getToWarehouseId())
                .orElseThrow(() -> new BadRequest("TO_WAREHOUSE_NOT_FOUND"));

        StockTransfer transfer =
                StockTransfer.builder()
                        .transferCode("TRF-" + System.currentTimeMillis())
                        .fromWarehouse(fromWarehouse)
                        .toWarehouse(toWarehouse)
                        .status(TransferStatus.DRAFT)
                        .createdAt(LocalDateTime.now())
                        .build();

        return stockTransferRepository.save(transfer);
    }

    public List<StockTransfer> getAllTransfers() {
        return stockTransferRepository.findAll();
    }

    public TransferResponse getTransferById(Long id) {

        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new BadRequest("TRANSFER_NOT_FOUND"));

        return TransferMapper.toResponse(transfer);
    }

    public StockTransfer approveTransfer(Long id) {

        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new BadRequest("TRANSFER_NOT_FOUND"));

        transfer.setStatus(TransferStatus.APPROVED);

        return stockTransferRepository.save(transfer);
    }

    public StockTransfer completeTransfer(Long id) {

        StockTransfer transfer = stockTransferRepository.findById(id)
                .orElseThrow(() -> new BadRequest("TRANSFER_NOT_FOUND"));

        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setCompletedAt(LocalDateTime.now());

        return stockTransferRepository.save(transfer);
    }
}