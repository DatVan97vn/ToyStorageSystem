import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { changePassword } from "../../api/auth/user";

function ChangePasswordMain() {
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
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

        if (newPassword.length < 6) {
            setError("Password must be at least 6 characters");
            return;
        }

        if (newPassword !== confirmPassword) {
            setError("Passwords do not match");
            return;
        }

        try {
            const res = await changePassword({
                newPassword,
            });

            if (res.data === "REQUIRE_2FA_SETUP") {
                navigate("/setup-2fa");
                return;
            }

            if (res.data === "REQUIRE_OTP") {
                navigate("/verify-otp");
                return;
            }

            navigate("/warehouse");
        } catch (err) {
            setError(getErrorMessage(err));
        }
    };

    return (
        <div className="auth-card">
            <h2>Change Password</h2>
            <p>You must change your password before continuing.</p>

            <form onSubmit={handleSubmit}>
                <input
                    type="password"
                    placeholder="New password"
                    value={newPassword}
                    autoComplete="new-password"
                    onChange={(e) => setNewPassword(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Confirm new password"
                    value={confirmPassword}
                    autoComplete="new-password"
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />

                {error && <p style={{ color: "red" }}>{error}</p>}

                <button type="submit">Change Password</button>
            </form>
        </div>
    );
}

export default ChangePasswordMain;