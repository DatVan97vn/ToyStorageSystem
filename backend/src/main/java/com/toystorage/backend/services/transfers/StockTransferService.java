package com.toystorage.backend.services.transfers;

import com.toystorage.backend.dto.request.transfers.CreateTransferRequest;
import com.toystorage.backend.enums.transfers.TransferStatus;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.repository.transfers.StockTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Service phiếu điều kho
 */

@Service

@RequiredArgsConstructor

public class StockTransferService {

    private final StockTransferRepository stockTransferRepository;

    /*
     * Tạo phiếu điều kho
     */
    public StockTransfer createTransfer(
            CreateTransferRequest request
    ) {

        StockTransfer transfer =
                StockTransfer.builder()

                        .transferCode(
                                "TRF-" + System.currentTimeMillis()
                        )

                        .status(TransferStatus.DRAFT)

                        .createdAt(LocalDateTime.now())

                        .build();

        return stockTransferRepository.save(transfer);
    }

    /*
     * Danh sách phiếu
     */
    public List<StockTransfer> getAllTransfers() {

        return stockTransferRepository.findAll();
    }

    /*
     * Chi tiết phiếu
     */
    public StockTransfer getTransferById(Long id) {

        return stockTransferRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transfer not found")
                );
    }

    /*
     * Approve phiếu
     */
    public StockTransfer approveTransfer(Long id) {

        StockTransfer transfer =
                getTransferById(id);

        transfer.setStatus(
                TransferStatus.APPROVED
        );

        return stockTransferRepository.save(transfer);
    }

    /*
     * Hoàn tất phiếu
     */
    public StockTransfer completeTransfer(Long id) {

        StockTransfer transfer =
                getTransferById(id);

        transfer.setStatus(
                TransferStatus.COMPLETED
        );

        transfer.setCompletedAt(
                LocalDateTime.now()
        );

        return stockTransferRepository.save(transfer);
    }
}