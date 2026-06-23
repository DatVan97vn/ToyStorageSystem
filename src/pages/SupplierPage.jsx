import { useEffect, useState } from "react";
import { getSuppliers } from "../api/supplierApi";

function SupplierPage() {
  const [suppliers, setSuppliers] = useState([]);

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

  return (
    <div>
      <h1>Danh sách nhà cung cấp</h1>

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
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
          {suppliers.length === 0 ? (
            <tr>
              <td colSpan="6">Không có dữ liệu nhà cung cấp</td>
            </tr>
          ) : (
            suppliers.map((supplier) => (
              <tr key={supplier.id}>
                <td>{supplier.id}</td>
                <td>{supplier.name}</td>
                <td>{supplier.phone}</td>
                <td>{supplier.email}</td>
                <td>{supplier.address}</td>
                <td>{supplier.taxCode}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default SupplierPage;