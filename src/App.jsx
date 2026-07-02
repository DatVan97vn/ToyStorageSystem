import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import LoginPage from './pages/auth/LoginPage';

import TwoFactorPage from './pages/auth/TwoFactorPage'; 
import ChangePasswordPage from './pages/auth/ChangePasswordPage';

import AdminLayout from './components/layout/AdminLayout';
import ProductPage from './pages/admin/ProductPage';
import EmployeePage from './pages/admin/EmployeePage';
import SupplierPage from './pages/admin/SupplierPage';
import StorePage from './pages/admin/StorePage';
import './index.css';

// 🔒 LỚP BẢO MẬT: Chỉ cho phép vào Admin khi ĐÃ ĐĂNG NHẬP
const ProtectedRoute = () => {
  const isAuthenticated = !!localStorage.getItem("token");
  return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
};

function App() {
  return (
    <Router>
      <Routes>
        
        {/* 🔓 THAY ĐỔI TẠI ĐÂY: Trang Login luôn luôn hiển thị đầu tiên khi vào web, không tự động nhảy trang nữa */}
        <Route path="/login" element={<LoginPage />} />

        {/* --- LUỒNG XÁC THỰC 2 LỚP (2FA) --- */}
        <Route path="/verify-2fa" element={<TwoFactorPage />} />

        {/* --- VÙNG AN TOÀN TUYỆT ĐỐI (BẮT BUỘC PHẢI CÓ TOKEN) --- */}
        <Route element={<ProtectedRoute />}>
          
          {/* Đổi mật khẩu */}
          <Route path="/change-password" element={<ChangePasswordPage />} />

          {/* Hệ thống quản trị Admin */}
          <Route path="/admin">
            <Route element={<AdminLayout />}>
              <Route index element={<Navigate to="products" replace />} />
              <Route path="products" element={<ProductPage />} />
              <Route path="employees" element={<EmployeePage />} />
              <Route path="suppliers" element={<SupplierPage />} />
              <Route path="stores" element={<StorePage />} />
            </Route>
          </Route>

        </Route>

        {/* --- TẤT CẢ CÁC ĐƯỜNG DẪN KHÁC ĐỀU BỊ ĐÁ VỀ TRANG LOGIN --- */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}

export default App;