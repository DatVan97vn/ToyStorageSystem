package com.toystorage.backend.services.inventory;

import com.toystorage.backend.dto.request.inventory.CreateInventoryPolicyRequest;
import com.toystorage.backend.dto.response.inventory.InventoryPolicyResponse;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.mapper.inventory.InventoryPolicyMapper;
import com.toystorage.backend.models.inventory.InventoryPolicy;
import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.warehouses.Warehouses;
import com.toystorage.backend.repository.inventory.InventoryPolicyRepository;
import com.toystorage.backend.repository.products.ProductRepository;
import com.toystorage.backend.repository.warehouses.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryPolicyService {

    private final InventoryPolicyRepository inventoryPolicyRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryPolicyMapper inventoryPolicyMapper;

    public InventoryPolicyResponse createPolicy(CreateInventoryPolicyRequest request) {
        if (request == null) {
            throw new BadRequest("INVENTORY_POLICY_REQUEST_REQUIRED");
        }

        if (request.getWarehouseId() == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        if (request.getProductId() == null) {
            throw new BadRequest("PRODUCT_ID_REQUIRED");
        }

        Warehouses warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new BadRequest("WAREHOUSE_NOT_FOUND"));

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BadRequest("PRODUCT_NOT_FOUND"));

        inventoryPolicyRepository
                .findByWarehouse_IdAndProduct_Id(warehouse.getId(), product.getId())
                .ifPresent(policy -> {
                    throw new BadRequest("INVENTORY_POLICY_ALREADY_EXISTS");
                });

        InventoryPolicy policy = InventoryPolicy.builder()
                .warehouse(warehouse)
                .product(product)
                .minimumQuantity(request.getMinimumQuantity())
                .maximumQuantity(request.getMaximumQuantity())
                .reorderQuantity(request.getReorderQuantity())
                .active(request.getActive())
                .build();

        return inventoryPolicyMapper.toResponse(
                inventoryPolicyRepository.save(policy)
        );
    }

    public InventoryPolicyResponse updatePolicy(Long id, CreateInventoryPolicyRequest request) {
        if (id == null) {
            throw new BadRequest("INVENTORY_POLICY_ID_REQUIRED");
        }

        InventoryPolicy policy = inventoryPolicyRepository.findById(id)
                .orElseThrow(() -> new BadRequest("INVENTORY_POLICY_NOT_FOUND"));

        if (request.getMinimumQuantity() != null) {
            policy.setMinimumQuantity(request.getMinimumQuantity());
        }

        if (request.getMaximumQuantity() != null) {
            policy.setMaximumQuantity(request.getMaximumQuantity());
        }

        if (request.getReorderQuantity() != null) {
            policy.setReorderQuantity(request.getReorderQuantity());
        }

        if (request.getActive() != null) {
            policy.setActive(request.getActive());
        }

        return inventoryPolicyMapper.toResponse(
                inventoryPolicyRepository.save(policy)
        );
    }

    public List<InventoryPolicyResponse> getAllPolicies() {
        return inventoryPolicyRepository.findAll()
                .stream()
                .map(inventoryPolicyMapper::toResponse)
                .toList();
    }

    public InventoryPolicyResponse getPolicyById(Long id) {
        return inventoryPolicyMapper.toResponse(
                inventoryPolicyRepository.findById(id)
                        .orElseThrow(() -> new BadRequest("INVENTORY_POLICY_NOT_FOUND"))
        );
    }

    public List<InventoryPolicyResponse> getPoliciesByWarehouse(Long warehouseId) {
        return inventoryPolicyRepository.findByWarehouse_Id(warehouseId)
                .stream()
                .map(inventoryPolicyMapper::toResponse)
                .toList();
    }

    public void deletePolicy(Long id) {
        InventoryPolicy policy = inventoryPolicyRepository.findById(id)
                .orElseThrow(() -> new BadRequest("INVENTORY_POLICY_NOT_FOUND"));

        inventoryPolicyRepository.delete(policy);
    }
}