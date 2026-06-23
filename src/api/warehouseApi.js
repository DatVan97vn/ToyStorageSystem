import api from "./api";

export const getWarehouses = () => api.get("/warehouses");