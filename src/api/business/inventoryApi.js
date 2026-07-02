import api from "../api";

export const getInventory = () => api.get("/inventory");

export const getInventoryByWarehouse = (warehouseId) =>
  api.get(`/inventory/warehouse/${warehouseId}`);

export const getInventoryByProduct = (productId) =>
  api.get(`/inventory/product/${productId}`);