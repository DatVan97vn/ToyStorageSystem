package com.toystorage.backend.controllers.dashboard;

import com.toystorage.backend.services.dashboard.DashboardWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * Dashboard kho
 */

@RestController

@RequestMapping("/api/dashboard/warehouse")

@RequiredArgsConstructor

public class DashboardWarehouseController {

    private final DashboardWarehouseService dashboardWarehouseService;

    /*
     * Dashboard KPI kho
     */
    @GetMapping

    public ResponseEntity<?> dashboard() {

        return ResponseEntity.ok(
                dashboardWarehouseService.getDashboard()
        );
    }
}