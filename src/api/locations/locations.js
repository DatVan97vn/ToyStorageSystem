import api from "../api";

export const getLocations = () =>
    api.get("/locations");

export const moveStockLocation = (data) =>
    api.post("/stock-locations/move", data);