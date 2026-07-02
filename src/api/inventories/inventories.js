import api from "../api";

export const getInventory = () =>
    api.get("/inventory");

export const getInventoryProduct = (productId) =>
    api.get(`/inventory/products/${productId}`);

export const getInventoryWarehouse = (warehouseId) =>
    api.get(`/inventory/warehouses/${warehouseId}`);