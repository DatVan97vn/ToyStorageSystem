import axios from "axios";

const API = "http://localhost:8080";

const axiosClient = axios.create({
  baseURL: API,
  withCredentials: true,
});

export const login = (username, password) => {
  return axiosClient.post(
    `/auth/login?username=${username}&password=${password}`
  );
};

export const setup2FA = (username) => {
  return axiosClient.get(`/2fa/setup?username=${username}`);
};

export const verify2FA = (code) => {
  return axiosClient.post(`/2fa/verify?code=${code}`);
};

export const login2FA = (code) => {
  return axiosClient.post(`/2fa/login?code=${code}`);
};

export const changePassword = (newPassword) => {
  return axiosClient.post(
    `/auth/change-password?newPassword=${newPassword}`
  );
};

export const logout = () => {
  return axiosClient.post("/auth/logout");
};

export const checkLogin = () => {
  return axiosClient.get("/auth/me");
};