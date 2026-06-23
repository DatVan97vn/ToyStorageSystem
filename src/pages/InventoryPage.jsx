import { useEffect, useState } from "react";
import { getInventory } from "../api/inventoryApi";

function InventoryPage() {
  const [inventories, setInventories] = useState([]);

  useEffect(() => {
    const loadInventory = async () => {
      try {
        const res = await getInventory();
        setInventories(res.data);
      } catch (error) {
        console.error("Lỗi lấy tồn kho:", error);
      }
    };

    loadInventory();
  }, []);

  return (
    <div>
      <h1>Danh sách tồn kho</h1>

      {inventories.length === 0 ? (
        <h3>Chưa có dữ liệu tồn kho</h3>
      ) : (
        <table border="1" cellPadding="10" cellSpacing="0" width="100%">
          <thead>
            <tr>
              <th>ID</th>
              <th>Kho</th>
              <th>Sản phẩm</th>
              <th>Số lượng</th>
            </tr>
          </thead>

          <tbody>
            {inventories.map((inventory) => (
              <tr key={inventory.id}>
                <td>{inventory.id}</td>
                <td>{inventory.warehouseName}</td>
                <td>{inventory.productName}</td>
                <td>{inventory.quantity}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default InventoryPage;