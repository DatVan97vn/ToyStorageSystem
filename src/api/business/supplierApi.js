import api from "../api";

export const getSuppliers = () => api.get("/suppliers");

export const getSupplierById = (id) => api.get(`/suppliers/${id}`);