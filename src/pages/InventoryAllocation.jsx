import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getInventory } from "../api/inventoryApi";
import { getWarehouses } from "../api/warehouseApi";

function InventoryAllocation() {
  const navigate = useNavigate();

  const [inventory, setInventory] = useState([]);
  const [warehouses, setWarehouses] = useState([]);
  const [loading, setLoading] = useState(false);

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

        row[warehouse.id] = stock ? stock.quantity : 0;
      });

      return row;
    });
  }, [productOptions, warehouses, inventory]);

  const handleGoToCreateTransfer = () => {
    navigate("/transfers/create");
  };

  return (
    <div style={pageStyle}>
      <h1>Điều phối hàng hóa</h1>
      <p style={{ color: "#9ca3af" }}>
        Business xem tồn kho giữa các kho/cửa hàng để quyết định điều chuyển hàng hóa.
      </p>

      <div style={statGridStyle}>
        <div style={statCardStyle}>
          <span style={statLabelStyle}>Tổng tồn kho</span>
          <strong style={statNumberStyle}>{totalQuantity}</strong>
        </div>

        <div style={statCardStyle}>
          <span style={statLabelStyle}>Sản phẩm có tồn</span>
          <strong style={statNumberStyle}>{totalProducts}</strong>
        </div>

        <div style={statCardStyle}>
          <span style={statLabelStyle}>Kho/Cửa hàng có hàng</span>
          <strong style={statNumberStyle}>{warehouseHasStock}</strong>
        </div>
      </div>

      <div style={sectionStyle}>
        <h2>Bảng tổng hợp tồn kho theo kho</h2>

        {loading ? (
          <p>Đang tải dữ liệu...</p>
        ) : inventoryMatrix.length === 0 ? (
          <p>Chưa có dữ liệu tồn kho</p>
        ) : (
          <table style={tableStyle}>
            <thead>
              <tr>
                <th style={thStyle}>Sản phẩm</th>
                {warehouses.map((warehouse) => (
                  <th key={warehouse.id} style={thStyle}>
                    {warehouse.name}
                  </th>
                ))}
              </tr>
            </thead>

            <tbody>
              {inventoryMatrix.map((row) => (
                <tr key={row.productId}>
                  <td style={tdStyle}>{row.productName}</td>
                  {warehouses.map((warehouse) => (
                    <td key={warehouse.id} style={tdStyle}>
                      {row[warehouse.id]}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div style={sectionStyle}>
        <h2>Tồn kho chi tiết</h2>

        {loading ? (
          <p>Đang tải dữ liệu...</p>
        ) : inventory.length === 0 ? (
          <p>Chưa có dữ liệu tồn kho</p>
        ) : (
          <table style={tableStyle}>
            <thead>
              <tr>
                <th style={thStyle}>ID</th>
                <th style={thStyle}>Sản phẩm</th>
                <th style={thStyle}>Kho/Cửa hàng</th>
                <th style={thStyle}>Số lượng tồn</th>
              </tr>
            </thead>

            <tbody>
              {inventory.map((item) => (
                <tr key={item.id}>
                  <td style={tdStyle}>{item.id}</td>
                  <td style={tdStyle}>{item.productName}</td>
                  <td style={tdStyle}>{item.warehouseName}</td>
                  <td style={tdStyle}>{item.quantity}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div style={sectionStyle}>
        <h2>Quyết định điều chuyển</h2>
        <p style={{ color: "#9ca3af" }}>
          Sau khi xem tồn kho, Business có thể chuyển sang màn tạo phiếu điều kho chính thức.
        </p>

        <button style={buttonStyle} type="button" onClick={handleGoToCreateTransfer}>
          ➕ Tạo phiếu điều kho
        </button>
      </div>
    </div>
  );
}

const pageStyle = {
  maxWidth: "1200px",
  margin: "0 auto",
};

const sectionStyle = {
  marginTop: "28px",
};

const statGridStyle = {
  display: "grid",
  gridTemplateColumns: "repeat(3, 1fr)",
  gap: "18px",
  marginTop: "24px",
};

const statCardStyle = {
  background: "white",
  color: "#111827",
  borderRadius: "12px",
  padding: "18px",
};

const buttonStyle = {
  marginTop: "10px",
  padding: "12px 20px",
  borderRadius: "8px",
  border: "none",
  cursor: "pointer",
  background: "#3b82f6",
  color: "white",
  fontWeight: "bold",
};

const tableStyle = {
  width: "100%",
  borderCollapse: "collapse",
};

const thStyle = {
  border: "1px solid #d1d5db",
  padding: "10px",
  textAlign: "left",
};

const tdStyle = {
  border: "1px solid #d1d5db",
  padding: "10px",
};

const statLabelStyle = {
  display: "block",
  color: "#4b5563",
  marginBottom: "10px",
};

const statNumberStyle = {
  display: "block",
  fontSize: "32px",
  fontWeight: "bold",
  color: "#111827",
};

export default InventoryAllocation;