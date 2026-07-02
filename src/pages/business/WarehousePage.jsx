import { useEffect, useMemo, useState } from "react";
import { getWarehouses } from "../../api/business/warehouseApi";

function WarehousePage() {
  const [warehouses, setWarehouses] = useState([]);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    const loadWarehouses = async () => {
      try {
        const res = await getWarehouses();
        setWarehouses(res.data);
      } catch (error) {
        console.error("Lỗi lấy danh sách kho:", error);
      }
    };

    loadWarehouses();
  }, []);

  const filteredWarehouses = useMemo(() => {
    const text = keyword.trim().toLowerCase();

    if (!text) return warehouses;

    return warehouses.filter((warehouse) =>
      [
        warehouse.id,
        warehouse.code,
        warehouse.name,
        warehouse.type,
        warehouse.phone,
        warehouse.address,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(text))
    );
  }, [keyword, warehouses]);

  const mainWarehouseCount = warehouses.filter(
    (item) => item.type === "MAIN_WAREHOUSE"
  ).length;

  const storeCount = warehouses.filter((item) => item.type === "STORE").length;

  return (
    <div className="page-container master-page">
      <div className="page-header">
        <div>
          <h1>Kho và cửa hàng</h1>
          <p>Danh sách kho trung tâm và cửa hàng nhận hàng trong hệ thống.</p>
        </div>
      </div>

      <div className="stat-grid master-stat-grid">
        <div className="stat-card">
          <span>Tổng địa điểm</span>
          <strong>{warehouses.length}</strong>
        </div>

        <div className="stat-card">
          <span>Kho chính</span>
          <strong>{mainWarehouseCount}</strong>
        </div>

        <div className="stat-card">
          <span>Cửa hàng</span>
          <strong>{storeCount}</strong>
        </div>
      </div>

      <div className="filter-bar">
        <input
          value={keyword}
          onChange={(event) => setKeyword(event.target.value)}
          placeholder="Tìm theo mã kho, tên kho, loại, địa chỉ..."
        />
      </div>

      <div className="table-wrapper master-table-wrapper">
        <table className="data-table master-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Mã kho</th>
              <th>Tên kho</th>
              <th>Loại</th>
              <th>Số điện thoại</th>
              <th>Địa chỉ</th>
            </tr>
          </thead>

          <tbody>
            {filteredWarehouses.length === 0 ? (
              <tr>
                <td className="empty-cell" colSpan="6">
                  Không có dữ liệu kho
                </td>
              </tr>
            ) : (
              filteredWarehouses.map((warehouse) => (
                <tr key={warehouse.id}>
                  <td>{warehouse.id}</td>
                  <td className="code-cell">{warehouse.code}</td>
                  <td className="strong-cell">{warehouse.name}</td>
                  <td>
                    <span
                      className={`type-badge ${
                        warehouse.type === "STORE" ? "store" : "main"
                      }`}
                    >
                      {warehouse.type === "STORE" ? "Cửa hàng" : "Kho chính"}
                    </span>
                  </td>
                  <td>{warehouse.phone || "-"}</td>
                  <td>{warehouse.address || "-"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default WarehousePage;