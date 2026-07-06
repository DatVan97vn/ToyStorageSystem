package com.toystorage.backend.services.inventory;

import com.toystorage.backend.dto.request.inventory.ApproveReplenishmentRequest;
import com.toystorage.backend.dto.request.inventory.CreateReplenishmentRequest;
import com.toystorage.backend.dto.response.inventory.ReplenishmentRequestResponse;
import com.toystorage.backend.enums.inventory.ReplenishmentStatus;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.inventory.ReplenishmentRequestMapper;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.inventory.ReplenishmentRequest;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.inventory.ReplenishmentRequestRepository;
import com.toystorage.backend.repository.products.ProductRepository;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplenishmentRequestService {

    private final ReplenishmentRequestRepository replenishmentRequestRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReplenishmentRequestMapper replenishmentRequestMapper;

    public ReplenishmentRequestResponse createRequest(CreateReplenishmentRequest request) {
        if (request == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_REQUIRED");
        }

        if (request.getRequestWarehouseId() == null) {
            throw new BadRequest("REQUEST_WAREHOUSE_ID_REQUIRED");
        }

        if (request.getProductId() == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        if (request.getRequestedQuantity() == null || request.getRequestedQuantity() <= 0) {
            throw new BadRequest("REQUESTED_QUANTITY_INVALID");
        }

        Warehouses requestWarehouse = warehouseRepository.findById(request.getRequestWarehouseId())
                .orElseThrow(() -> new BadRequest("REQUEST_WAREHOUSE_NOT_FOUND"));

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BadRequest("PRODUCT_NOT_FOUND"));

        User requestedBy = null;

        if (request.getRequestedById() != null) {
            requestedBy = userRepository.findById(request.getRequestedById())
                    .orElseThrow(() -> new BadRequest("REQUEST_USER_NOT_FOUND"));
        }

        ReplenishmentRequest entity = ReplenishmentRequest.builder()
                .requestWarehouse(requestWarehouse)
                .product(product)
                .currentQuantity(request.getCurrentQuantity())
                .requestedQuantity(request.getRequestedQuantity())
                .requestedBy(requestedBy)
                .note(request.getNote())
                .status(ReplenishmentStatus.PENDING)
                .build();

        return replenishmentRequestMapper.toResponse(
                replenishmentRequestRepository.save(entity)
        );
    }

    public ReplenishmentRequestResponse approveRequest(
            Long id,
            ApproveReplenishmentRequest request
    ) {
        if (id == null) {
            throw new BadRequest("REPLENISHMENT_REQUEST_ID_REQUIRED");
        }

        if (request == null) {
            throw new BadRequest("APPROVE_REQUEST_REQUIRED");
        }

        ReplenishmentRequest entity = replenishmentRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequest("REPLENISHMENT_REQUEST_NOT_FOUND"));

        Warehouses sourceWarehouse = warehouseRepository.findById(request.getSourceWarehouseId())
                .orElseThrow(() -> new BadRequest("SOURCE_WAREHOUSE_NOT_FOUND"));

        User approvedBy = userRepository.findById(request.getApprovedById())
                .orElseThrow(() -> new BadRequest("APPROVER_NOT_FOUND"));

        entity.setSourceWarehouse(sourceWarehouse);
        entity.setApprovedBy(approvedBy);
        entity.setApprovedQuantity(
                request.getApprovedQuantity() != null
                        ? request.getApprovedQuantity()
                        : entity.getRequestedQuantity()
        );
        entity.setNote(request.getNote());
        entity.setStatus(ReplenishmentStatus.APPROVED);
        entity.setApprovedAt(LocalDateTime.now());

        return replenishmentRequestMapper.toResponse(
                replenishmentRequestRepository.save(entity)
        );
    }

    public ReplenishmentRequestResponse rejectRequest(Long id, String note) {
        ReplenishmentRequest entity = replenishmentRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequest("REPLENISHMENT_REQUEST_NOT_FOUND"));

        entity.setStatus(ReplenishmentStatus.REJECTED);
        entity.setNote(note);

        return replenishmentRequestMapper.toResponse(
                replenishmentRequestRepository.save(entity)
        );
    }

    public ReplenishmentRequestResponse markTransferCreated(Long id) {
        ReplenishmentRequest entity = replenishmentRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequest("REPLENISHMENT_REQUEST_NOT_FOUND"));

        entity.setStatus(ReplenishmentStatus.TRANSFER_CREATED);

        return replenishmentRequestMapper.toResponse(
                replenishmentRequestRepository.save(entity)
        );
    }

    public ReplenishmentRequestResponse completeRequest(Long id) {
        ReplenishmentRequest entity = replenishmentRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequest("REPLENISHMENT_REQUEST_NOT_FOUND"));

        entity.setStatus(ReplenishmentStatus.COMPLETED);
        entity.setCompletedAt(LocalDateTime.now());

        return replenishmentRequestMapper.toResponse(
                replenishmentRequestRepository.save(entity)
        );
    }

    public List<ReplenishmentRequestResponse> getAllRequests() {
        return replenishmentRequestRepository.findAll()
                .stream()
                .map(replenishmentRequestMapper::toResponse)
                .toList();
    }

    public ReplenishmentRequestResponse getRequestById(Long id) {
        return replenishmentRequestMapper.toResponse(
                replenishmentRequestRepository.findById(id)
                        .orElseThrow(() -> new BadRequest("REPLENISHMENT_REQUEST_NOT_FOUND"))
        );
    }

    public List<ReplenishmentRequestResponse> getRequestsByWarehouse(Long warehouseId) {
        return replenishmentRequestRepository.findByRequestWarehouse_Id(warehouseId)
                .stream()
                .map(replenishmentRequestMapper::toResponse)
                .toList();
    }

    public List<ReplenishmentRequestResponse> getRequestsByStatus(ReplenishmentStatus status) {
        return replenishmentRequestRepository.findByStatus(status)
                .stream()
                .map(replenishmentRequestMapper::toResponse)
                .toList();
    }
}