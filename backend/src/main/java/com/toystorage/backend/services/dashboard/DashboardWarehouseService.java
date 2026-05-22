package com.toystorage.backend.services.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/*
 * Service dashboard kho
 */

@Service

@RequiredArgsConstructor

public class DashboardWarehouseService {

    /*
     * Dashboard KPI
     */
    public Map<String, Object> getDashboard() {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "totalTransfers",
                100
        );

        data.put(
                "completedTransfers",
                80
        );

        data.put(
                "pendingTransfers",
                20
        );

        return data;
    }
}