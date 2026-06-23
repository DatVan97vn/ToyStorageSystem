import { useEffect, useState } from "react";
import { getWarehouses } from "../api/warehouseApi";

function WarehousePage() {
  const [warehouses, setWarehouses] = useState([]);

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

  return (
    <div>
      <h1>Danh sách kho</h1>

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
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
          {warehouses.length === 0 ? (
            <tr>
              <td colSpan="6">Không có dữ liệu</td>
            </tr>
          ) : (
            warehouses.map((warehouse) => (
              <tr key={warehouse.id}>
                <td>{warehouse.id}</td>
                <td>{warehouse.code}</td>
                <td>{warehouse.name}</td>
                <td>{warehouse.type}</td>
                <td>{warehouse.phone}</td>
                <td>{warehouse.address}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default WarehousePage;