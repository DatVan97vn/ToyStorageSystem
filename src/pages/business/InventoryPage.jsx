import { useEffect, useMemo, useState } from "react";
import { getInventory } from "../../api/business/inventoryApi";

function InventoryPage() {
  const [inventories, setInventories] = useState([]);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    const loadInventory = async () => {
      try {
        const res = await getInventory();
        setInventories(res.data);
      } catch (error) {
        console.error("Lỗi lấy danh sách tồn kho:", error);
      }
    };

    loadInventory();
  }, []);

  const filteredInventories = useMemo(() => {
    const text = keyword.trim().toLowerCase();

    if (!text) return inventories;

    return inventories.filter((item) =>
      [
        item.id,
        item.warehouseName,
        item.warehouse,
        item.productName,
        item.product,
        item.quantity,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(text))
    );
  }, [keyword, inventories]);

  const totalQuantity = inventories.reduce((sum, item) => {
    return sum + Number(item.quantity || 0);
  }, 0);

  const lowStockCount = inventories.filter(
    (item) => Number(item.quantity || 0) <= 10
  ).length;

  const getWarehouseName = (item) => {
    if (item.warehouseName) return item.warehouseName;
    if (item.warehouse?.name) return item.warehouse.name;
    return "-";
  };

  const getProductName = (item) => {
    if (item.productName) return item.productName;
    if (item.product?.name) return item.product.name;
    return "-";
  };

  const getStockStatus = (quantity) => {
    const number = Number(quantity || 0);

    if (number <= 10) return "low";
    if (number <= 50) return "medium";
    return "good";
  };

  const getStockLabel = (quantity) => {
    const number = Number(quantity || 0);

    if (number <= 10) return "Sắp hết";
    if (number <= 50) return "Trung bình";
    return "Còn nhiều";
  };

  return (
    <div className="page-container master-page">
      <div className="page-header">
        <div>
          <h1>Tồn kho</h1>
          <p>Theo dõi số lượng sản phẩm đang tồn tại từng kho và cửa hàng.</p>
        </div>
      </div>

      <div className="stat-grid master-stat-grid">
        <div className="stat-card">
          <span>Tổng dòng tồn</span>
          <strong>{inventories.length}</strong>
        </div>

        <div className="stat-card">
          <span>Tổng số lượng</span>
          <strong>{totalQuantity}</strong>
        </div>

        <div className="stat-card">
          <span>Sắp hết hàng</span>
          <strong>{lowStockCount}</strong>
        </div>
      </div>

      <div className="filter-bar">
        <input
          value={keyword}
          onChange={(event) => setKeyword(event.target.value)}
          placeholder="Tìm theo kho, sản phẩm hoặc số lượng..."
        />
      </div>

      <div className="table-wrapper master-table-wrapper">
        <table className="data-table master-table inventory-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Kho</th>
              <th>Sản phẩm</th>
              <th>Số lượng</th>
              <th>Trạng thái</th>
            </tr>
          </thead>

          <tbody>
            {filteredInventories.length === 0 ? (
              <tr>
                <td className="empty-cell" colSpan="5">
                  Không có dữ liệu tồn kho
                </td>
              </tr>
            ) : (
              filteredInventories.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td className="strong-cell">{getWarehouseName(item)}</td>
                  <td>{getProductName(item)}</td>
                  <td className="quantity-cell">{item.quantity}</td>
                  <td>
                    <span
                      className={`stock-badge ${getStockStatus(
                        item.quantity
                      )}`}
                    >
                      {getStockLabel(item.quantity)}
                    </span>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default InventoryPage;