import { useState, useEffect } from "react";
//import api from "../../services/api";
import { dashboardStyles as s } from "../../styles/dashboard.styles"; 

const AdminDashboard = () => {
  const [totalUsers, setTotalUsers] = useState(0);
  const [activeSessions, setActiveSessions] = useState(0);
  const [auditLogs, setAuditLogs] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  // Viết hàm đồng bộ dữ liệu khi bấm nút bấm tay bên ngoài
  const handleRefresh = async () => {
    setIsLoading(true);
    try {
      // Giả lập gọi API tải lại dữ liệu
      setTimeout(() => {
        setTotalUsers(32);
        setActiveSessions(4);
        setIsLoading(false);
      }, 500);
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  };

  // ✅ Khắc phục triệt để lỗi "cascading renders" bằng cách gom hàm chạy lần đầu vào đây
  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        // Sau này có backend: const response = await api.get('/dashboard-stats');
        setTimeout(() => {
          setTotalUsers(32);
          setActiveSessions(4);
          setAuditLogs([
            { id: 101, action: 'CREATE_USER', details: 'Tạo tài khoản nhân viên kho mới: "kho_quan10"', createdBy: 'admin_it', createdAt: '2026-06-25 14:32:10' },
            { id: 102, action: 'LOCK_USER', details: 'Khóa tài khoản tài xế do vi phạm bảo mật: "driver_lan"', createdBy: 'admin_it', createdAt: '2026-06-25 11:15:04' },
            { id: 103, action: 'CREATE_WAREHOUSE', details: 'Thiết lập điểm phân phối mới: Cửa hàng đồ chơi Quận 3', createdBy: 'admin_it', createdAt: '2026-06-24 16:45:22' },
            { id: 104, action: 'UPDATE_CONFIG', details: 'Changed configuration format for stock transfer slip codes', createdBy: 'admin_it', createdAt: '2026-06-24 09:20:00' }
          ]);
          setIsLoading(false);
        }, 500);
      } catch (error) {
        console.error("Lỗi khi gọi API dashboard:", error);
        setIsLoading(false);
      }
    };

    loadDashboardData();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    window.location.href = "/login";
  };

  return (
    <div className={s.container}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <div>
          <h2 className={s.titleHeader}>Admin IT Monitoring Dashboard</h2>
          <p className={s.subtitleHeader}>Real-time monitoring of system account allocation, active sessions, and security audit logs.</p>
        </div>
        <button onClick={handleLogout} style={{ padding: '8px 16px', backgroundColor: '#ef4444', color: '#fff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold' }}>
          Đăng xuất 🚪
        </button>
      </div>

      {/* Hiển thị số liệu tổng hợp */}
      <div style={{ display: 'flex', gap: '20px', marginBottom: '24px' }}>
        <div style={{ flex: 1, padding: '20px', backgroundColor: '#fff', borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)' }}>
          <span style={{ fontSize: '14px', color: '#6b7280' }}>Tổng tài khoản</span>
          <h3 style={{ fontSize: '24px', fontWeight: 'bold', margin: '5px 0 0 0' }}>{totalUsers}</h3>
        </div>
        <div style={{ flex: 1, padding: '20px', backgroundColor: '#fff', borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)' }}>
          <span style={{ fontSize: '14px', color: '#6b7280' }}>Phiên hoạt động</span>
          <h3 style={{ fontSize: '24px', fontWeight: 'bold', margin: '5px 0 0 0' }}>{activeSessions} <span style={{ color: '#10b981', fontSize: '14px' }}>Online</span></h3>
        </div>
      </div>

      {/* Bảng Nhật ký logs */}
      <div style={{ backgroundColor: '#fff', borderRadius: '8px', boxShadow: '0 1px 3px rgba(0,0,0,0.1)', padding: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <h3 style={{ margin: 0, fontSize: '16px' }}>Nhật ký bảo mật hệ thống (Audit Logs)</h3>
          <button onClick={handleRefresh} style={{ padding: '6px 12px', backgroundColor: '#059669', color: '#fff', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
            🔄 Đồng bộ dữ liệu
          </button>
        </div>

        {isLoading ? (
          <div style={{ textAlign: 'center', padding: '20px', color: '#6b7280' }}>⏳ Đang tải nhật ký bảo mật...</div>
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '14px', textAlign: 'left' }}>
            <thead>
              <tr style={{ backgroundColor: '#f9fafb' }}>
                <th style={{ padding: '12px', borderBottom: '1px solid #e5e7eb' }}>Hành động</th>
                <th style={{ padding: '12px', borderBottom: '1px solid #e5e7eb' }}>Chi tiết hoạt động</th>
                <th style={{ padding: '12px', borderBottom: '1px solid #e5e7eb' }}>Người thực hiện</th>
                <th style={{ padding: '12px', borderBottom: '1px solid #e5e7eb' }}>Thời gian</th>
              </tr>
            </thead>
            <tbody>
              {auditLogs.map((log) => (
                <tr key={log.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                  <td style={{ padding: '12px' }}><span style={{ padding: '4px 8px', backgroundColor: '#dbeafe', color: '#1e40af', borderRadius: '4px', fontSize: '12px', fontWeight: 'bold' }}>{log.action}</span></td>
                  <td style={{ padding: '12px', fontWeight: 'bold' }}>{log.details}</td>
                  <td style={{ padding: '12px' }}>{log.createdBy}</td>
                  <td style={{ padding: '12px' }}>{log.createdAt}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;