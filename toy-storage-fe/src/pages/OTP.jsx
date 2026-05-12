import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login2FA } from "../services/authService";

function OTP() {

  const [code, setCode] = useState("");
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  const handleVerify = async (e) => {
    e.preventDefault();

    try {

      const res = await login2FA(code);

      if (res.data === "LOGIN SUCCESS") {

        setMessage("Xác thực OTP thành công");

        setTimeout(() => {
          navigate("/home");
        }, 1000);

      } else {

        setMessage("Mã OTP không đúng");

      }

    } catch (error) {

      setMessage("Lỗi xác thực OTP");
      console.error(error);

    }
  };

  return (
    <div className="container">

      <form className="card" onSubmit={handleVerify}>

        <h2>Nhập mã OTP</h2>

        <input
          type="text"
          placeholder="Mã 6 số"
          value={code}
          onChange={(e) => setCode(e.target.value)}
        />

        <button type="submit">
          Verify OTP
        </button>

        <p>{message}</p>

      </form>

    </div>
  );
}

export default OTP;