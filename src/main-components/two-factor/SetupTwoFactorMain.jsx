import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import TwoFactorCard from "../two-factor/TwoFactorCard";
import OtpInput from "../../components/two-factor/OtpInput";
import {
    setupTwoFactor,
    verifyTwoFactorSetup,
} from "../../api/auth/user";

function SetupTwoFactorMain() {
    const [qr, setQr] = useState("");
    const [code, setCode] = useState("");
    const [error, setError] = useState("");
    const didRun = useRef(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (didRun.current) return;
        didRun.current = true;

        const loadQrCode = async () => {
            try {
                const res = await setupTwoFactor();
                setQr(`data:image/png;base64,${res.data.qr}`);
            } catch (err) {
                console.log("2FA setup error:", err.response?.data);

                const data = err.response?.data;
                setError(
                    typeof data === "string"
                        ? data
                        : data?.message || data?.error || "Failed to generate 2FA QR code"
                );
            }
        };

        loadQrCode();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        if (!/^\d{6}$/.test(code)) {
            setError("OTP code must contain 6 digits");
            return;
        }

        try {
            const res = await verifyTwoFactorSetup(code);

            if (res.data === "LOGIN_SUCCESS") {
                navigate("/warehouse");
            }
        } catch (err) {
            const data = err.response?.data;
            setError(
                typeof data === "string"
                    ? data
                    : data?.message || data?.error || "Invalid or expired OTP code"
            );
        }
    };

    return (
        <TwoFactorCard
            title="Set up two-factor authentication"
            description="Scan the QR code with Google Authenticator or Microsoft Authenticator."
            qr={qr}
        >
            <form onSubmit={handleSubmit}>
                <OtpInput value={code} onChange={setCode} />
                {error && <p style={{ color: "red" }}>{error}</p>}
                <button type="submit">Verify</button>
            </form>
        </TwoFactorCard>
    );
}

export default SetupTwoFactorMain;