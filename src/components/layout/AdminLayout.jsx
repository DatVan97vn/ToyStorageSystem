import { Outlet, Link } from 'react-router-dom';

const AdminLayout = () => {
  return (
    <div style={{ 
      display: 'flex', 
      position: 'fixed', 
      top: 0,
      left: 0,
      width: '100%',    
      height: '100%',
      margin: 0, 
      padding: 0,
      overflow: 'hidden' 
    }}>
      {/* Sidebar */}
      <nav style={{ 
        width: '250px', 
        backgroundColor: '#2c3e50', 
        color: '#fff', 
        padding: '20px',
        height: '100%', 
        flexShrink: 0 
      }}>
        <h2>Toy Admin</h2>
        <ul style={{ listStyle: 'none', padding: 0, marginTop: '30px' }}>
          <li style={{ marginBottom: '15px' }}>
            <Link to="/admin/products" style={{ color: '#fff', textDecoration: 'none' }}>📦 Products</Link>
          </li>
          <li style={{ marginBottom: '15px' }}>
            <Link to="/admin/employees" style={{ color: '#fff', textDecoration: 'none' }}>👥 Employees</Link>
          </li>
          <li style={{ marginBottom: '15px' }}>
            <Link to="/admin/suppliers" style={{ color: '#fff', textDecoration: 'none' }}>🏭 Suppliers</Link>
          </li>
          <li style={{ marginBottom: '15px' }}>
            <Link to="/admin/stores" style={{ color: '#fff', textDecoration: 'none' }}>🏪 Stores</Link>
          </li>
        </ul>
      </nav>

      {/* Main Content */}
      <main style={{ 
        flex: 1, 
        padding: '30px', 
        backgroundColor: '#f4f7f6',
        overflowY: 'auto' 
      }}>
        <Outlet /> 
      </main>
    </div>
  );
};

export default AdminLayout;