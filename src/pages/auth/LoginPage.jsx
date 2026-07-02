import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios'; // 🔥 Thêm thư viện axios để gọi API

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false); // 🔥 State trạng thái chờ Backend
    const [errorMessage, setErrorMessage] = useState(''); // 🔥 State hiển thị lỗi lên màn hình
    
    const navigate = useNavigate();

    const toys = [
        { icon: '🧸', top: '12%', left: '8%', size: '35px', delay: '0s' },
        { icon: '🧩', top: '45%', left: '6%', size: '32px', delay: '2s' },
        { icon: '🚀', top: '80%', left: '10%', size: '40px', delay: '1.5s' },
        { icon: '🚂', top: '28%', left: '22%', size: '35px', delay: '0.8s' },
        { icon: '🎈', top: '68%', left: '25%', size: '34px', delay: '2.2s' },
        { icon: '🦖', top: '52%', left: '14%', size: '36px', delay: '2.7s' },
        { icon: '🚗', top: '15%', left: '88%', size: '30px', delay: '1s' },
        { icon: '🎮', top: '50%', left: '90%', size: '36px', delay: '0.5s' },
        { icon: '🤖', top: '82%', left: '84%', size: '38px', delay: '2.5s' },
        { icon: '🛹', top: '25%', left: '68%', size: '30px', delay: '1.2s' },
        { icon: '⚽', top: '65%', left: '72%', size: '32px', delay: '1.7s' },
        { icon: '🎨', top: '40%', left: '78%', size: '30px', delay: '1.4s' },
        { icon: '🪁', top: '6%', left: '48%', size: '32px', delay: '3s' },
        { icon: '🦄', top: '88%', left: '50%', size: '42px', delay: '0s' }
    ];

    // 🔥 HÀM ĐĂNG NHẬP KẾT NỐI BACKEND ĐÍNH KÈM BẢO MẬT
    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setErrorMessage(''); 

        try {
            
            const response = await axios.post('http://100.124.247.52:8080/api/auth/login', {
                email: email,
                password: password
            });

            // 🗝️ Hứng Token bảo mật từ Backend trả về
            const token = response.data; 
            const user = response.data.user;

           if (token) {
    // 💾 Lưu token và user vào máy như cũ
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));

    console.log("Đăng nhập bước 1 thành công! Đang chuyển sang trang 2FA...");
    
   
    navigate('/verify-2fa'); 
} else {
    setErrorMessage('Không tìm thấy Token hợp lệ từ hệ thống!');
}

        } catch (error) {
            console.error("Lỗi đăng nhập:", error);
            // Hiển thị lỗi từ Backend trả về hoặc lỗi mất kết nối mạng
            if (error.response && error.response.data) {
                setErrorMessage(error.response.data.message || 'Tài khoản hoặc mật khẩu không chính xác!');
            } else {
                setErrorMessage('Không thể kết nối đến máy chủ Backend!');
            }
        } finally {
            setLoading(false); // Mở khóa lại nút bấm
        }
    };

    return (
        <div style={containerStyle}>
            <style>{`
                @keyframes floatAnimation {
                    0% { transform: translateY(0px) rotate(0deg); }
                    50% { transform: translateY(-15px) rotate(5deg); }
                    100% { transform: translateY(0px) rotate(0deg); }
                }
            `}</style>

            <div style={toyWrapperStyle}>
                {toys.map((toy, index) => (
                    <div 
                        key={index} 
                        style={{
                            position: 'absolute',
                            top: toy.top,
                            left: toy.left,
                            fontSize: toy.size,
                            animation: `floatAnimation 4s ease-in-out infinite`,
                            animationDelay: toy.delay,
                            userSelect: 'none'
                        }}
                    >
                        {toy.icon}
                    </div>
                ))}
            </div>

            <div style={cardStyle}>
                <div style={logoContainer}>
                    <span style={{ fontSize: '42px' }}>🤖</span>
                    <h2 style={titleStyle}>TOY STORAGE SYSTEM</h2>
                </div>

                {/* 🔥 KHU VỰC HIỂN THỊ LỖI NẾU ĐĂNG NHẬP THẤT BẠI */}
                {errorMessage && (
                    <div style={errorBannerStyle}>
                        ⚠️ {errorMessage}
                    </div>
                )}

                <form onSubmit={handleLogin} style={{ width: '100%' }}>
                    <div style={inputGroup}>
                        <label style={labelStyle}>Admin Account</label>
                        <input 
                            type="email" 
                            style={inputStyle} 
                            placeholder="phuc@gmail.com" 
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            disabled={loading} // Đang loading thì không cho sửa
                            required 
                        />
                    </div>

                    <div style={inputGroup}>
                        <label style={labelStyle}>Password</label>
                        <input 
                            type="password" 
                            style={inputStyle} 
                            placeholder="••••••" 
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            disabled={loading} // Đang loading thì không cho sửa
                            required 
                        />
                    </div>

                    {/* 🔥 NÚT BẤM THÔNG MINH: Tự đổi chữ và tự khóa khi đang load */}
                    <button 
                        type="submit" 
                        style={{...btnStyle, opacity: loading ? 0.7 : 1, cursor: loading ? 'not-allowed' : 'pointer'}}
                        disabled={loading}
                    >
                        {loading ? 'Connecting Backend...' : 'Continue 🚀'}
                    </button>
                </form>
            </div>
        </div>
    );
};

// --- HỆ THỐNG STYLE CSS FULL ---
const containerStyle = {
    position: 'fixed',
    top: 0, left: 0, right: 0, bottom: 0,
    backgroundImage: 'linear-gradient(to right, #ff9966, #ff5e62)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'flex-start', 
    overflowY: 'auto',        
    padding: '40px 15px',     
    boxSizing: 'border-box',
    fontFamily: '"Segoe UI", Tahoma, Geneva, Verdana, sans-serif',
    zIndex: 99999
};

const toyWrapperStyle = {
    position: 'absolute',
    top: 0, left: 0, right: 0, bottom: 0,
    overflow: 'hidden',    
    pointerEvents: 'none', 
    zIndex: 1
};

const cardStyle = {
    backgroundColor: '#ffffff',
    padding: '30px 25px',     
    borderRadius: '16px',
    boxShadow: '0 10px 25px rgba(0, 0, 0, 0.1)',
    width: '100%',
    maxWidth: '360px',
    margin: 'auto',           
    zIndex: 10,            
    boxSizing: 'border-box'
};

// Style cho dòng thông báo lỗi
const errorBannerStyle = {
    backgroundColor: '#fdebd0',
    color: '#e67e22',
    padding: '10px',
    borderRadius: '8px',
    fontSize: '12px',
    fontWeight: 'bold',
    marginBottom: '15px',
    textAlign: 'center',
    border: '1px solid #f5b041'
};

const logoContainer = { textAlign: 'center', marginBottom: '15px' };
const titleStyle = { color: '#d35400', fontSize: '18px', fontWeight: 'bold', marginTop: '6px', letterSpacing: '0.5px' };
const inputGroup = { marginBottom: '14px', width: '100%' };
const labelStyle = { display: 'block', color: '#7f8c8d', fontSize: '11px', marginBottom: '4px', fontWeight: 'bold' };

const inputStyle = {
    width: '100%',
    padding: '11px 12px',
    borderRadius: '8px',
    border: '1px solid #eaeded',
    backgroundColor: '#ebf5fb',
    boxSizing: 'border-box',
    outline: 'none',
    fontSize: '14px'
};

const btnStyle = {
    width: '100%',
    padding: '12px',
    backgroundColor: '#ff6b6b',
    color: 'white',
    border: 'none',
    borderRadius: '8px',
    fontSize: '15px',
    fontWeight: 'bold',
    marginTop: '8px',
    boxShadow: '0 4px 15px rgba(255, 107, 107, 0.3)',
    transition: 'all 0.2s ease'
};

export default LoginPage;