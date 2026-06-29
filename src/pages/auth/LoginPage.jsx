import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../api/auth/user";

function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

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

    const handleLogin = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const res = await login({
                email: email.trim(),
                password,
            });

            console.log("Login response:", res.data);

            if (res.data === "REQUIRE_CHANGE_PASSWORD") {
                navigate("/change-password");
                return;
            }

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
            console.log("Login error:", err.response?.data);
            setError(getErrorMessage(err));
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h2>Login</h2>

                <form onSubmit={handleLogin}>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        autoComplete="email"
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        autoComplete="current-password"
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    {error && <p style={{ color: "red" }}>{error}</p>}

                    <button type="submit">Login</button>
                </form>
            </div>
        </div>
    );
}

export default LoginPage;