import api from "../api";

export const createShippingManifest = (data) =>
    api.post("/manifests", data);

export const getShippingManifests = () =>
    api.get("/manifests");

export const getShippingManifestById = (id) =>
    api.get(`/manifests/${id}`);

export const getShippingManifestByCode = (manifestCode) =>
    api.get(`/manifests/code/${manifestCode}`);

export const getManifestsByFromWarehouse = (warehouseId) =>
    api.get(`/manifests/from-warehouse/${warehouseId}`);

export const getManifestsByToWarehouse = (warehouseId) =>
    api.get(`/manifests/to-warehouse/${warehouseId}`);

export const updateManifestStatus = (id, status) =>
    api.put(`/manifests/${id}/status/${status}`);

export const addPackageToManifest = (manifestId, packageId) =>
    api.post(`/manifests/${manifestId}/packages/${packageId}`);

export const getPackagesByManifest = (manifestId) =>
    api.get(`/manifests/${manifestId}/packages`);

export const removePackageFromManifest = (manifestId, packageId) =>
    api.delete(`/manifests/${manifestId}/packages/${packageId}`);