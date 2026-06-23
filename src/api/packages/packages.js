import api from "../api.js";

export const createPackage = (data) =>
    api.post("/packages", data);

export const getPackages = () =>
    api.get("/packages");

export const getPackageById = (id) =>
    api.get(`/packages/${id}`);

export const addTransferToPackage = (packageId, transferId) =>
    api.post(`/packages/${packageId}/add-transfer`, {
        transferId,
    });

export const addItemToPackage = (packageId, data) =>
    api.post(`/packages/${packageId}/add-item`, data);

export const closePackage = (id) =>
    api.put(`/packages/${id}/close`);
export const getPackagesByTransfer = (transferId) =>
    api.get(`/packages/transfer/${transferId}`);