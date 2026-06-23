import api from "./api";

export const getTransfers = () => api.get("/transfers");

export const getTransferById = (id) => api.get(`/transfers/${id}`);

export const createTransfer = (data) => api.post("/transfers", data);

export const approveTransfer = (id) => api.put(`/transfers/${id}/approve`);

export const completeTransfer = (id) => api.put(`/transfers/${id}/complete`);