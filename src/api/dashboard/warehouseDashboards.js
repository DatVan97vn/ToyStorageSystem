import api from "../api";

export const getWarehouseSummary = () =>
    api.get("/warehouse-dashboard/summary");

export const getStaffPerformance = () =>
    api.get("/warehouse-dashboard/staff-performance");

export const getCheckingTime = () =>
    api.get("/warehouse-dashboard/checking-time");

export const getPendingTransfers = () =>
    api.get("/warehouse-dashboard/pending-transfers");

export const getCompletedTransfers = () =>
    api.get("/warehouse-dashboard/completed-transfers");