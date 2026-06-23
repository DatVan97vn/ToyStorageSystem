import { NavLink } from "react-router-dom";

function Sidebar() {
  return (
    <aside style={sidebarStyle}>
      <div style={brandStyle}>
        <h2 style={titleStyle}>TOY STORAGE</h2>
        <p style={subTitleStyle}>Business Management</p>
      </div>

      <nav style={navStyle}>
        <NavLink to="/" style={getLinkStyle}>
          🏠 Dashboard
        </NavLink>

        <NavLink to="/transfers" style={getLinkStyle}>
          📦 Phiếu điều kho
        </NavLink>

        <NavLink to="/transfers/create" style={getLinkStyle}>
          ➕ Tạo phiếu
        </NavLink>

        <NavLink to="/transfers/tracking" style={getLinkStyle}>
          📈 Theo dõi phiếu
        </NavLink>

        <NavLink to="/products" style={getLinkStyle}>
          🧸 Sản phẩm
        </NavLink>

        <NavLink to="/warehouses" style={getLinkStyle}>
          🏭 Kho
        </NavLink>

        <NavLink to="/suppliers" style={getLinkStyle}>
          🚚 Nhà cung cấp
        </NavLink>

        <NavLink to="/inventory" style={getLinkStyle}>
          📊 Tồn kho
        </NavLink>

        <NavLink to="/inventory/allocation" style={getLinkStyle}>
          🔁 Điều phối hàng
        </NavLink>

        <NavLink to="/transfers/import-excel" style={getLinkStyle}>
          📄 Import Excel
        </NavLink>
        <NavLink to="/goods-receipts" style={getLinkStyle}>
          📥 Phiếu nhập hàng
        </NavLink>
        <NavLink to="/packages" style={getLinkStyle}>
          📦 Thùng kiện
        </NavLink>
        <NavLink to="/warehouse-scan" style={getLinkStyle}>
          📷 Scan QR
        </NavLink>
      </nav>
      
    </aside>
  );
}

const sidebarStyle = {
  width: "260px",
  minWidth: "260px",
  minHeight: "100vh",
  background: "linear-gradient(180deg, #ff7900 0%, #f97316 100%)",
  padding: "28px 14px",
  boxSizing: "border-box",
  color: "white",
  boxShadow: "8px 0 24px rgba(0, 0, 0, 0.25)",
};

const brandStyle = {
  padding: "0 10px 28px",
  marginBottom: "18px",
  borderBottom: "1px solid rgba(255, 255, 255, 0.22)",
};

const titleStyle = {
  margin: 0,
  fontSize: "26px",
  fontWeight: "800",
  letterSpacing: "0.5px",
};

const subTitleStyle = {
  margin: "8px 0 0",
  fontSize: "14px",
  opacity: 0.9,
};

const navStyle = {
  display: "flex",
  flexDirection: "column",
  gap: "8px",
};

const linkStyle = {
  display: "flex",
  alignItems: "center",
  width: "100%",
  minHeight: "48px",
  boxSizing: "border-box",
  color: "white",
  textDecoration: "none",
  fontSize: "16px",
  padding: "10px 14px",
  borderRadius: "12px",
  lineHeight: "1.2",
  fontWeight: "700",
  transition: "0.2s ease",
};

const activeStyle = {
  backgroundColor: "white",
  color: "#f97316",
  boxShadow: "0 8px 18px rgba(0, 0, 0, 0.18)",
};

function getLinkStyle({ isActive }) {
  return {
    ...linkStyle,
    ...(isActive ? activeStyle : {}),
  };
}

export default Sidebar;