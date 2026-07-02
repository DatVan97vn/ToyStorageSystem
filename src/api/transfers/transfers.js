import api from "../api";

export const getTransfers = () =>
    api.get("/transfers");

export const getTransferById = (id) =>
    api.get(`/transfers/${id}`);

export const createTransfer = (data) =>
    api.post("/transfers", data);

export const approveTransfer = (id) =>
    api.put(`/transfers/${id}/approve`);

export const completeTransfer = (id) =>
    api.put(`/transfers/${id}/complete`);

export const updateTransferStatus = (id, status) =>
    api.put(`/transfers/${id}/status/${status}`);

export const scanTransferBarcode = (id, data) =>
    api.post("/scans", {
        transferId: Number(id),
        barcode: data.barcode,
        quantity: Number(data.quantity) || 1,
        scanType: data.scanType || "PICK",
    });