import api from "../api";

// Lấy QR để setup 2FA
export const setupTwoFactor = () =>
    api.post("/2fa/setup");

// Xác minh mã OTP lần đầu sau khi quét QR
export const verifyTwoFactorSetup = (code) =>
    api.post("/2fa/verify", null, {
        params: { code },
    });

// Đăng nhập bằng mã OTP nếu đã bật 2FA
export const loginTwoFactor = (code) =>
    api.post("/2fa/login", null, {
        params: { code },
    });
export const login = (data) =>
    api.post("/auth/login", data);

export const changePassword = (data) =>
    api.post("/auth/change-password", data);

export const getMe = () =>
    api.get("/auth/me");

export const logout = () =>
    api.post("/auth/logout");

