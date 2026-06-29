import { useState } from "react";
import { useNavigate } from "react-router-dom";
import TwoFactorCard from "../two-factor/TwoFactorCard";
import OtpInput from "../../components/two-factor/OtpInput";
import { loginTwoFactor } from "../../api/auth/user";

function VerifyOtpMain() {
    const [code, setCode] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    const getErrorMessage = (err) => {
        const data = err.response?.data;

        if (!data) return "Something went wrong";
        if (typeof data === "string") return data;
        if (data.message) return data.message;
        if (data.error) return data.error;

        return "Something went wrong";
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        if (!/^\d{6}$/.test(code)) {
            setError("OTP code must contain 6 digits");
            return;
        }

        try {
            const res = await loginTwoFactor(code);

            if (res.data === "LOGIN_SUCCESS") {
                navigate("/warehouse");
            }
        } catch (err) {
            console.log("OTP login error:", err.response?.data);

            const data = err.response?.data;

            if (typeof data === "string") {
                setError(data);
            } else {
                setError(data?.message || data?.error || "Invalid or expired OTP code");
            }
        }
    };

    return (
        <TwoFactorCard
            title="Verify OTP"
            description="Enter the 6-digit code from your authenticator app."
        >
            <form onSubmit={handleSubmit}>
                <OtpInput value={code} onChange={setCode} />

                {error && <p style={{ color: "red" }}>{error}</p>}

                <button type="submit">Log in</button>
            </form>
        </TwoFactorCard>
    );
}

export default VerifyOtpMain;