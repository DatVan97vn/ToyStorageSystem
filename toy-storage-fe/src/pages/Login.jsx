import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/authService";

function Login() {
  const [username, setUsername] = useState("admin");
  const [password, setPassword] = useState("123");
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const res = await login(username, password);

      if (res.data === "REQUIRE_CHANGE_PASSWORD") {
      navigate("/change-password");
    } else if (res.data === "REQUIRE_2FA_SETUP") {
      navigate("/setup-2fa");
    } else if (res.data === "REQUIRE_OTP") {
      navigate("/otp");
    } else if (res.data === "LOGIN FAILED") {
      setMessage("Sai tài khoản hoặc mật khẩu");
    } else {
      setMessage(res.data);
    }
    } catch (error) {
      setMessage("Lỗi kết nối BE");
      console.error(error);
    }
  };

  return (
    <div className="container">
      <form className="card" onSubmit={handleLogin}>
        <h2>Login</h2>

        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button type="submit">Login</button>

        <p>{message}</p>
      </form>
    </div>
  );
}

export default Login;