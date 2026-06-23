import axios from "axios";

const api = axios.create({
    baseURL: "http://100.124.247.52:8080/api",
});

export default api;

