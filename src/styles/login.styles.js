export const loginStyles = {
  container: {
    // 🚀 Thay position: 'relative' bằng 'fixed' để nó chiếm trọn view cửa sổ
    position: 'fixed', 
    top: 0,
    left: 0,
    width: '100vw',
    height: '100vh',
    // 🎨 Màu sắc và căn chỉnh
    backgroundColor: '#ff7a00',
    backgroundImage: 'linear-gradient(135deg, #ff9f43 0%, #ff6b6b 100%)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    fontFamily: '"Segoe UI", Roboto, sans-serif',
    overflow: 'hidden', // Quan trọng: chặn không cho các icon nhảy múa tạo thanh cuộn
    zIndex: 1,
  },
  // ... giữ nguyên phần card, logo, title... (không cần sửa)
  card: {
    backgroundColor: '#ffffff',
    padding: '40px 35px',
    borderRadius: '24px',
    boxShadow: '0 15px 35px rgba(0, 0, 0, 0.2)',
    width: '90%', // Để linh hoạt hơn trên điện thoại
    maxWidth: '380px',
    textAlign: 'center',
    zIndex: 10,
    border: '5px solid rgba(255, 255, 255, 0.8)',
  },
  logo: {
    fontSize: '50px',
    margin: '0 0 10px 0',
    display: 'inline-block',
  },
  title: {
    fontSize: '24px',
    fontWeight: '800',
    color: '#d35400',
    margin: '0 0 6px 0',
    textTransform: 'uppercase',
    letterSpacing: '0.5px',
  },
  subtitle: {
    fontSize: '14px',
    color: '#7f8c8d',
    margin: '0 0 30px 0',
    fontWeight: '500',
  },
  inputGroup: {
    marginBottom: '22px',
    textAlign: 'left',
  },
  label: {
    display: 'block',
    fontSize: '13px',
    fontWeight: '700',
    color: '#e67e22',
    marginBottom: '8px',
  },
  input: {
    width: '100%',
    padding: '14px 16px',
    borderRadius: '12px',
    border: '2px solid #ffe0cc',
    fontSize: '15px',
    outline: 'none',
    boxSizing: 'border-box',
    transition: 'all 0.3s ease',
    backgroundColor: '#fff7f2',
  },
  button: {
    width: '100%',
    padding: '14px',
    backgroundColor: '#ff6b6b',
    color: '#ffffff',
    border: 'none',
    borderRadius: '12px',
    fontSize: '16px',
    fontWeight: 'bold',
    cursor: 'pointer',
    boxShadow: '0 6px 20px rgba(255, 107, 107, 0.4)',
    transition: 'all 0.2s ease',
    marginTop: '10px',
  }
};