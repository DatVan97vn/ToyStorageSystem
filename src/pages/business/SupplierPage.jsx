import { useEffect, useMemo, useState } from "react";
import { getSuppliers } from "../../api/business/supplierApi";

function SupplierPage() {
  const [suppliers, setSuppliers] = useState([]);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    const loadSuppliers = async () => {
      try {
        const res = await getSuppliers();
        setSuppliers(res.data);
      } catch (error) {
        console.error("Lỗi lấy danh sách nhà cung cấp:", error);
      }
    };

    loadSuppliers();
  }, []);

  const filteredSuppliers = useMemo(() => {
    const text = keyword.trim().toLowerCase();

    if (!text) return suppliers;

    return suppliers.filter((supplier) =>
      [
        supplier.id,
        supplier.name,
        supplier.phone,
        supplier.email,
        supplier.address,
        supplier.taxCode,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(text))
    );
  }, [keyword, suppliers]);

  return (
    <div className="page-container master-page">
      <div className="page-header">
        <div>
          <h1>Nhà cung cấp</h1>
          <p>Quản lý thông tin liên hệ, địa chỉ và mã số thuế của nhà cung cấp.</p>
        </div>
      </div>

      <div className="stat-grid master-stat-grid">
        <div className="stat-card">
          <span>Tổng nhà cung cấp</span>
          <strong>{suppliers.length}</strong>
        </div>

        <div className="stat-card">
          <span>Có email</span>
          <strong>{suppliers.filter((item) => item.email).length}</strong>
        </div>

        <div className="stat-card">
          <span>Có mã số thuế</span>
          <strong>{suppliers.filter((item) => item.taxCode).length}</strong>
        </div>
      </div>

      <div className="filter-bar">
        <input
          value={keyword}
          onChange={(event) => setKeyword(event.target.value)}
          placeholder="Tìm theo tên, SĐT, email, địa chỉ..."
        />
      </div>

      <div className="table-wrapper master-table-wrapper">
        <table className="data-table master-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tên nhà cung cấp</th>
              <th>Số điện thoại</th>
              <th>Email</th>
              <th>Địa chỉ</th>
              <th>Mã số thuế</th>
            </tr>
          </thead>

          <tbody>
            {filteredSuppliers.length === 0 ? (
              <tr>
                <td className="empty-cell" colSpan="6">
                  Không có dữ liệu nhà cung cấp
                </td>
              </tr>
            ) : (
              filteredSuppliers.map((supplier) => (
                <tr key={supplier.id}>
                  <td>{supplier.id}</td>
                  <td className="strong-cell">{supplier.name}</td>
                  <td>{supplier.phone || "-"}</td>
                  <td>{supplier.email || "-"}</td>
                  <td>{supplier.address || "-"}</td>
                  <td>{supplier.taxCode || "-"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default SupplierPage;