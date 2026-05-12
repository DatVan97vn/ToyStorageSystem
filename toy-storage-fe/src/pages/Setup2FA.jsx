import { useState } from "react";
import { setup2FA, verify2FA } from "../services/authService";
import { useNavigate } from "react-router-dom";

function Setup2FA() {
  const [username, setUsername] = useState("admin");
  const [qr, setQr] = useState("");
  const [code, setCode] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleSetup = async () => {
    try {
      const res = await setup2FA(username);
      setQr(res.data.qr);
      setMessage("Quét QR bằng Google Authenticator");
    } catch (error) {
      setMessage("Không tạo được QR");
      console.error(error);
    }
  };

  const handleVerify = async () => {
    try {
      const res = await verify2FA(code);

      if (res.data === "2FA ENABLED") {
        setMessage("Bật 2FA thành công. Đang quay về đăng nhập...");

        setTimeout(() => {
          navigate("/");
        }, 1000);
      } else {
        setMessage(res.data);
      }
    } catch (error) {
      setMessage("Lỗi verify OTP");
      console.error(error);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Setup Google Authenticator</h2>

        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <button onClick={handleSetup}>Tạo QR</button>

        {qr && (
          <img
            className="qr"
            src={`data:image/png;base64,${qr}`}
            alt="QR Code"
          />
        )}

        <input
          type="text"
          placeholder="Nhập mã 6 số"
          value={code}
          onChange={(e) => setCode(e.target.value)}
        />

        <button onClick={handleVerify}>Bật 2FA</button>

        <p>{message}</p>
      </div>
    </div>
  );
}

export default Setup2FA;