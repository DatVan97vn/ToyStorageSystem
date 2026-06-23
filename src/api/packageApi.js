import api from "./api";

export const getPackages = () => api.get("/packages");

export const createPackage = (data) => api.post("/packages", data);

export const getPackageTransfers = (packageId) =>
  api.get(`/package-transfers/package/${packageId}`);