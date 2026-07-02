import api from "../api";

export const getGoodsReceipts = () => api.get("/goods-receipts");

export const getGoodsReceiptById = (id) => api.get(`/goods-receipts/${id}`);

export const getGoodsReceiptItems = (id) =>
  api.get(`/goods-receipts/${id}/items`);

export const createGoodsReceipt = (data) =>
  api.post("/goods-receipts", data);

export const startGoodsReceipt = (id) =>
  api.put(`/goods-receipts/${id}/start`);

export const completeGoodsReceipt = (id) =>
  api.put(`/goods-receipts/${id}/complete`);

export const cancelGoodsReceipt = (id) =>
  api.put(`/goods-receipts/${id}/cancel`);

export const receiveGoodsReceiptItem = (itemId, data) =>
  api.put(`/goods-receipts/items/${itemId}/receive`, data);