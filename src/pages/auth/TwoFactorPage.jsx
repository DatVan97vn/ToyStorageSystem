import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import OtpInput from 'react-otp-input';
import { QRCodeCanvas } from 'qrcode.react';

export default function TwoFactorPage() {
  const [otp, setOtp] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleVerifyOTP = async (e) => {
    e.preventDefault();
    
    if (otp.length < 6) {
      setErrorMessage('Vui lòng nhập đủ 6 chữ số!');
      return;
    }

    setLoading(true);
    setErrorMessage('');

    try {
      // 🕵️ Lấy dữ liệu user một cách an toàn, tránh bị crash JSON
      const storedUser = localStorage.getItem('user');
      const userEmail = (storedUser && storedUser !== 'undefined') ? JSON.parse(storedUser)?.email : '';

      // 🌐 Gọi lệnh tới backend của Lâm Định để kiểm tra mã OTP
      await axios.post('http://100.124.247.52:8080/api/auth/verify-2fa', {
        code: otp,
        email: userEmail
      });

      console.log("Xác thực bước 2 bảo mật thành công!");
      navigate('/admin');

    } catch (error) {
      console.error("Lỗi xác thực 2FA:", error);
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data.message || 'Mã OTP không chính xác hoặc đã hết hạn!');
      } else {
        setErrorMessage('Không thể kết nối đến máy chủ kiểm tra 2FA!');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex h-screen flex-col items-center justify-center bg-gray-50">
      <form onSubmit={handleVerifyOTP} className="bg-white p-8 rounded-xl shadow-lg w-full max-w-sm text-center">
        <h2 className="text-xl font-bold mb-4">Xác thực 2-Lớp (2FA)</h2>
        
        <div className="flex justify-center mb-4">
          {/* Đã cập nhật chuỗi Secret mã hóa chuẩn Base32 để điện thoại hiển thị số quay vòng */}
          <QRCodeCanvas value="otpauth://totp/ToyStorage?secret=NBSWY3DPEB3W64TBNQ" />
        </div>
        <p className="text-sm text-gray-600 mb-6">Nhập mã 6 số từ ứng dụng Google Authenticator:</p>
        
        {errorMessage && (
          <div className="mb-4 text-sm text-red-600 bg-red-50 p-2 rounded border border-red-200">
            {errorMessage}
          </div>
        )}
        
        <div className="flex justify-center">
          <OtpInput
            value={otp}
            onChange={setOtp}
            numInputs={6}
            renderInput={(props) => (
              <input 
                {...props} 
                disabled={loading}
                className="w-10 h-10 border border-gray-300 rounded mx-1 text-center font-bold text-lg focus:border-blue-500 focus:outline-none disabled:bg-gray-100" 
              />
            )}
          />
        </div>
        
        <button 
          type="submit"
          disabled={loading}
          className={`mt-6 w-full text-white py-2 rounded transition-colors font-medium ${
            loading ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700'
          }`}
        >
          {loading ? 'Đang kiểm tra...' : 'Xác nhận'}
        </button>
      </form>
    </div>
  );
}