package com.toystorage.backend.controllers.inventory;

import com.toystorage.backend.dto.request.inventory.PutawayRequest;
import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.services.inventory.PutawayTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/putaway-tasks")
@RequiredArgsConstructor
public class PutawayTaskController {

    private final PutawayTaskService putawayTaskService;

    @PostMapping
    public ResponseEntity<?> putaway(
            @RequestBody PutawayRequest request
    ) {
        if (request == null) {
            throw new BadRequest("PUTAWAY_REQUEST_REQUIRED");
        }

        return ResponseEntity.ok(
                putawayTaskService.putaway(request)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(
                putawayTaskService.getAllTasks()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(
            @PathVariable Long id
    ) {
        if (id == null) {
            throw new BadRequest("PUTAWAY_TASK_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                putawayTaskService.getTaskById(id)
        );
    }

    @GetMapping("/receipt/{receiptId}")
    public ResponseEntity<?> getTasksByReceipt(
            @PathVariable Long receiptId
    ) {
        if (receiptId == null) {
            throw new BadRequest("GOODS_RECEIPT_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                putawayTaskService.getTasksByReceipt(receiptId)
        );
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<?> getTasksByWarehouse(
            @PathVariable Long warehouseId
    ) {
        if (warehouseId == null) {
            throw new BadRequest("WAREHOUSE_ID_REQUIRED");
        }

        return ResponseEntity.ok(
                putawayTaskService.getTasksByWarehouse(warehouseId)
        );
    }
}