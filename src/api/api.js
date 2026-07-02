import axios from "axios";

const api = axios.create({
    baseURL: "/api",
    withCredentials: true,
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const message = error.response?.data;

        if (message === "NOT_LOGIN" || message === "NOT_AUTHENTICATED") {
            window.location.href = "/login";
        }

        return Promise.reject(error);
    }
);

export default api;
