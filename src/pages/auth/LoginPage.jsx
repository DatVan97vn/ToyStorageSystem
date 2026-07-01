import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../api/auth/user";
import "../../assets/css/auth/Login.css";
function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

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
        setLoading(true);

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
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <div className="login-card">
                <h1 className="login-title">Toy Storage System</h1>

                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            autoComplete="email"
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Password</label>
                        <input
                            type="password"
                            placeholder="Enter your password"
                            value={password}
                            autoComplete="current-password"
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    {error && <div className="login-error">{error}</div>}

                    <button className="login-btn" disabled={loading}>
                        {loading ? "Signing in..." : "Login"}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default LoginPage;