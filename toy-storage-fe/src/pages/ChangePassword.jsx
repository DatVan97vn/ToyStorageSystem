import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { changePassword } from "../services/authService";

function ChangePassword() {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  const handleChangePassword = async (e) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      setMessage("Mật khẩu xác nhận không khớp");
      return;
    }

    try {
      const res = await changePassword(newPassword);

      if (res.data === "PASSWORD CHANGED") {
        setMessage("Đổi mật khẩu thành công");

        setTimeout(() => {
          navigate("/setup-2fa");
        }, 1000);
      } else {
        setMessage(res.data);
      }
    } catch (error) {
      setMessage("Lỗi đổi mật khẩu");
      console.error(error);
    }
  };

  return (
    <div className="container">
      <form className="card" onSubmit={handleChangePassword}>
        <h2>Đổi mật khẩu lần đầu</h2>

        <input
          type="password"
          placeholder="Mật khẩu mới"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
        />

        <input
          type="password"
          placeholder="Nhập lại mật khẩu"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />

        <button type="submit">Đổi mật khẩu</button>

        <p>{message}</p>
      </form>
    </div>
  );
}

export default ChangePassword;