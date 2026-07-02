import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function ChangePasswordPage() {
  const [pass, setPass] = useState({ newPass: '', confirmPass: '' });
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (pass.newPass !== pass.confirmPass) {
      alert("Mật khẩu không khớp!");
      return;
    }
    alert("Đổi mật khẩu thành công!");
    localStorage.setItem('token', 'fake-token-123'); // Lưu token giả để vào admin
    navigate("/admin");
  };

  return (
    <div className="flex h-screen items-center justify-center bg-gray-50">
      <form onSubmit={handleSubmit} className="bg-white p-10 rounded-2xl shadow-xl w-96">
        <h2 className="text-2xl font-bold mb-6 text-center">Thiết lập mật khẩu mới</h2>
        <input className="w-full p-3 border mb-4 rounded-lg" type="password" placeholder="Mật khẩu mới" 
          onChange={(e) => setPass({...pass, newPass: e.target.value})} />
        <input className="w-full p-3 border mb-6 rounded-lg" type="password" placeholder="Xác nhận mật khẩu" 
          onChange={(e) => setPass({...pass, confirmPass: e.target.value})} />
        <button className="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700">Cập nhật</button>
      </form>
    </div>
  );
}