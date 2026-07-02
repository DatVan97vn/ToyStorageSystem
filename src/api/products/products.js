import api from "../api";

export const getProductByBarcode = (barcode) =>
    api.get(`/products/barcode/${barcode}`);

export const getProductLocations = (productId) =>
    api.get(`/products/${productId}/locations`);