import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getInventory } from "../../api/business/inventoryApi";
import { getWarehouses } from "../../api/business/warehouseApi";

function InventoryAllocation() {
  const navigate = useNavigate();

  const [inventory, setInventory] = useState([]);
  const [warehouses, setWarehouses] = useState([]);
  const [loading, setLoading] = useState(false);

  const [keyword, setKeyword] = useState("");
  const [warehouseFilter, setWarehouseFilter] = useState("ALL");
  const [stockFilter, setStockFilter] = useState("ALL");

  const loadData = async () => {
    try {
      setLoading(true);

      const inventoryRes = await getInventory();
      const warehouseRes = await getWarehouses();

      setInventory(Array.isArray(inventoryRes.data) ? inventoryRes.data : []);
      setWarehouses(Array.isArray(warehouseRes.data) ? warehouseRes.data : []);
    } catch (error) {
      console.error("Lỗi tải dữ liệu điều phối:", error);
      alert("Không thể tải dữ liệu tồn kho");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const productOptions = useMemo(() => {
    const map = new Map();

    inventory.forEach((item) => {
      if (!map.has(item.productId)) {
        map.set(item.productId, {
          productId: item.productId,
          productName: item.productName,
        });
      }
    });

    return Array.from(map.values());
  }, [inventory]);

  const totalQuantity = inventory.reduce(
    (sum, item) => sum + Number(item.quantity || 0),
    0
  );

  const totalProducts = productOptions.length;

  const warehouseHasStock = new Set(
    inventory
      .filter((item) => Number(item.quantity) > 0)
      .map((item) => item.warehouseId)
  ).size;

  const getStockStatus = (quantity) => {
    const number = Number(quantity || 0);

    if (number === 0) return "empty";
    if (number <= 5) return "low";
    if (number <= 20) return "medium";
    return "good";
  };

  const getStockLabel = (quantity) => {
    const status = getStockStatus(quantity);

    if (status === "empty") return "Hết hàng";
    if (status === "low") return "Tồn thấp";
    if (status === "medium") return "Cần theo dõi";
    return "Ổn định";
  };

  const getAllocationSuggestion = (row) => {
    const emptyStores = [];
    const lowStores = [];

    warehouses.forEach((warehouse) => {
      const quantity = Number(row[warehouse.id] || 0);
      const isStore = warehouse.type === "STORE";

      if (!isStore) return;

      if (quantity === 0) {
        emptyStores.push(warehouse.name);
      } else if (quantity <= 5) {
        lowStores.push(warehouse.name);
      }
    });

    if (emptyStores.length > 0) {
      return {
        type: "danger",
        text: `Cần bổ sung: ${emptyStores.join(", ")}`,
      };
    }

    if (lowStores.length > 0) {
      return {
        type: "warning",
        text: `Tồn thấp: ${lowStores.join(", ")}`,
      };
    }

    return {
      type: "success",
      text: "Tồn kho ổn định",
    };
  };

  const inventoryMatrix = useMemo(() => {
    return productOptions.map((product) => {
      const row = {
        productId: product.productId,
        productName: product.productName,
      };

      warehouses.forEach((warehouse) => {
        const stock = inventory.find(
          (item) =>
            item.productId === product.productId &&
            item.warehouseId === warehouse.id
        );

        row[warehouse.id] = stock ? Number(stock.quantity || 0) : 0;
      });

      return row;
    });
  }, [productOptions, warehouses, inventory]);

  const filteredMatrix = useMemo(() => {
    const text = keyword.trim().toLowerCase();

    return inventoryMatrix.filter((row) => {
      const matchKeyword =
        !text || row.productName.toLowerCase().includes(text);

      const matchWarehouse =
        warehouseFilter === "ALL" ||
        Number(row[warehouseFilter] || 0) > 0;

      const matchStock =
        stockFilter === "ALL" ||
        warehouses.some((warehouse) => {
          const quantity = Number(row[warehouse.id] || 0);
          const status = getStockStatus(quantity);

          return status === stockFilter;
        });

      return matchKeyword && matchWarehouse && matchStock;
    });
  }, [inventoryMatrix, keyword, warehouseFilter, stockFilter, warehouses]);

  const filteredInventory = useMemo(() => {
    const text = keyword.trim().toLowerCase();

    return inventory.filter((item) => {
      const matchKeyword =
        !text ||
        String(item.productName || "").toLowerCase().includes(text) ||
        String(item.warehouseName || "").toLowerCase().includes(text);

      const matchWarehouse =
        warehouseFilter === "ALL" ||
        String(item.warehouseId) === String(warehouseFilter);

      const matchStock =
        stockFilter === "ALL" ||
        getStockStatus(item.quantity) === stockFilter;

      return matchKeyword && matchWarehouse && matchStock;
    });
  }, [inventory, keyword, warehouseFilter, stockFilter]);

  const handleGoToCreateTransfer = () => {
    navigate("/business/transfers/create");
  };

  return (
    <div className="page-container allocation-page">
      <div className="allocation-header">
        <div>
          <h1>Điều phối hàng hóa</h1>
          <p>
            Business xem tồn kho giữa các kho/cửa hàng để quyết định điều
            chuyển hàng hóa.
          </p>
        </div>

        <button
          type="button"
          className="allocation-refresh-btn"
          onClick={loadData}
        >
          Làm mới
        </button>
      </div>

      <div className="stat-grid master-stat-grid">
        <div className="stat-card">
          <span>Tổng tồn kho</span>
          <strong>{totalQuantity}</strong>
        </div>

        <div className="stat-card">
          <span>Sản phẩm có tồn</span>
          <strong>{totalProducts}</strong>
        </div>

        <div className="stat-card">
          <span>Kho/Cửa hàng có hàng</span>
          <strong>{warehouseHasStock}</strong>
        </div>
      </div>

      <div className="allocation-card">
        <div className="allocation-card-title">
          <h2>Bộ lọc điều phối</h2>
          <span>Lọc dữ liệu</span>
        </div>

        <div className="allocation-filter-grid">
          <div className="form-group">
            <label>Tìm sản phẩm / kho</label>
            <input
              value={keyword}
              onChange={(event) => setKeyword(event.target.value)}
              placeholder="Nhập tên sản phẩm hoặc kho..."
            />
          </div>

          <div className="form-group">
            <label>Kho/Cửa hàng</label>
            <select
              value={warehouseFilter}
              onChange={(event) => setWarehouseFilter(event.target.value)}
            >
              <option value="ALL">Tất cả kho/cửa hàng</option>
              {warehouses.map((warehouse) => (
                <option key={warehouse.id} value={warehouse.id}>
                  {warehouse.name}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Trạng thái tồn</label>
            <select
              value={stockFilter}
              onChange={(event) => setStockFilter(event.target.value)}
            >
              <option value="ALL">Tất cả trạng thái</option>
              <option value="empty">Hết hàng</option>
              <option value="low">Tồn thấp</option>
              <option value="medium">Cần theo dõi</option>
              <option value="good">Ổn định</option>
            </select>
          </div>
        </div>
      </div>

      <div className="allocation-card">
        <div className="allocation-card-title">
          <h2>Bảng tổng hợp tồn kho theo kho</h2>
          <span>{filteredMatrix.length} sản phẩm</span>
        </div>

        {loading ? (
          <p className="allocation-muted">Đang tải dữ liệu...</p>
        ) : filteredMatrix.length === 0 ? (
          <p className="allocation-muted">Chưa có dữ liệu tồn kho</p>
        ) : (
          <div className="allocation-table-scroll">
            <table className="allocation-table">
              <thead>
                <tr>
                  <th>Sản phẩm</th>
                  {warehouses.map((warehouse) => (
                    <th key={warehouse.id}>{warehouse.name}</th>
                  ))}
                  <th>Gợi ý điều phối</th>
                </tr>
              </thead>

              <tbody>
                {filteredMatrix.map((row) => {
                  const suggestion = getAllocationSuggestion(row);

                  return (
                    <tr key={row.productId}>
                      <td className="strong-cell">{row.productName}</td>

                      {warehouses.map((warehouse) => {
                        const quantity = Number(row[warehouse.id] || 0);
                        const status = getStockStatus(quantity);

                        return (
                          <td key={warehouse.id}>
                            <span className={`stock-badge ${status}`}>
                              {quantity}
                            </span>
                          </td>
                        );
                      })}

                      <td>
                        <span className={`suggestion-badge ${suggestion.type}`}>
                          {suggestion.text}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="allocation-card">
        <div className="allocation-card-title">
          <h2>Tồn kho chi tiết</h2>
          <span>{filteredInventory.length} dòng</span>
        </div>

        {loading ? (
          <p className="allocation-muted">Đang tải dữ liệu...</p>
        ) : filteredInventory.length === 0 ? (
          <p className="allocation-muted">Chưa có dữ liệu tồn kho</p>
        ) : (
          <div className="allocation-table-scroll">
            <table className="allocation-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Sản phẩm</th>
                  <th>Kho/Cửa hàng</th>
                  <th>Số lượng tồn</th>
                  <th>Trạng thái</th>
                </tr>
              </thead>

              <tbody>
                {filteredInventory.map((item) => {
                  const status = getStockStatus(item.quantity);

                  return (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td className="strong-cell">{item.productName}</td>
                      <td>{item.warehouseName}</td>
                      <td className="quantity-cell">{item.quantity}</td>
                      <td>
                        <span className={`stock-badge ${status}`}>
                          {getStockLabel(item.quantity)}
                        </span>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="allocation-card allocation-action-card">
        <div>
          <h2>Quyết định điều chuyển</h2>
          <p>
            Sau khi xem tồn kho, Business có thể chuyển sang màn tạo phiếu điều
            kho chính thức.
          </p>
        </div>

        <button
          type="button"
          className="allocation-create-btn"
          onClick={handleGoToCreateTransfer}
        >
          + Tạo phiếu điều kho
        </button>
      </div>
    </div>
  );
}

export default InventoryAllocation;