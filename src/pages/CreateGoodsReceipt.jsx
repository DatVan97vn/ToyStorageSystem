import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getSuppliers } from "../api/supplierApi";
import { getWarehouses } from "../api/warehouseApi";
import { getProducts } from "../api/productApi";
import { createGoodsReceipt } from "../api/goodsReceiptApi";

function CreateGoodsReceipt() {
  const navigate = useNavigate();

  const [suppliers, setSuppliers] = useState([]);
  const [warehouses, setWarehouses] = useState([]);
  const [products, setProducts] = useState([]);

  const [supplierId, setSupplierId] = useState("");
  const [warehouseId, setWarehouseId] = useState("");

  const [selectedProductId, setSelectedProductId] = useState("");
  const [expectedQuantity, setExpectedQuantity] = useState(1);
  const [items, setItems] = useState([]);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [supplierRes, warehouseRes, productRes] = await Promise.all([
          getSuppliers(),
          getWarehouses(),
          getProducts(),
        ]);

        setSuppliers(Array.isArray(supplierRes.data) ? supplierRes.data : []);
        setWarehouses(Array.isArray(warehouseRes.data) ? warehouseRes.data : []);
        setProducts(Array.isArray(productRes.data) ? productRes.data : []);
      } catch (error) {
        console.error("Lỗi tải dữ liệu tạo phiếu nhập:", error);
        alert("Không tải được dữ liệu tạo phiếu nhập");
      }
    };

    loadData();
  }, []);

  const handleAddItem = () => {
    if (!selectedProductId || Number(expectedQuantity) <= 0) {
      alert("Vui lòng chọn sản phẩm và nhập số lượng hợp lệ");
      return;
    }

    const product = products.find((p) => p.id === Number(selectedProductId));

    if (!product) {
      alert("Không tìm thấy sản phẩm");
      return;
    }

    const existedIndex = items.findIndex(
      (item) => item.productId === Number(selectedProductId)
    );

    if (existedIndex !== -1) {
      const updatedItems = [...items];
      updatedItems[existedIndex].expectedQuantity += Number(expectedQuantity);
      setItems(updatedItems);
    } else {
      setItems([
        ...items,
        {
          productId: product.id,
          productName: product.name,
          sku: product.sku,
          barcode: product.barcode,
          unit: product.unit,
          expectedQuantity: Number(expectedQuantity),
        },
      ]);
    }

    setSelectedProductId("");
    setExpectedQuantity(1);
  };

  const handleRemoveItem = (index) => {
    setItems(items.filter((_, i) => i !== index));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!supplierId) {
      alert("Vui lòng chọn nhà cung cấp");
      return;
    }

    if (!warehouseId) {
      alert("Vui lòng chọn kho nhận");
      return;
    }

    if (items.length === 0) {
      alert("Vui lòng thêm ít nhất một sản phẩm");
      return;
    }

    const payload = {
      supplierId: Number(supplierId),
      warehouseId: Number(warehouseId),
      items: items.map((item) => ({
        productId: item.productId,
        expectedQuantity: item.expectedQuantity,
      })),
    };

    try {
      console.log("PAYLOAD PHIẾU NHẬP:", payload);

      const res = await createGoodsReceipt(payload);
      console.log("Tạo phiếu nhập thành công:", res.data);

      alert("Tạo phiếu nhập hàng thành công");
      navigate("/goods-receipts");
    } catch (error) {
      console.error("Lỗi tạo phiếu nhập:", error);
      alert("Tạo phiếu nhập hàng thất bại");
    }
  };

  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto" }}>
      <h1>Tạo phiếu nhập hàng</h1>
      <p style={{ color: "#9ca3af" }}>
        Business tạo phiếu nhập hàng từ nhà cung cấp vào kho.
      </p>

      <form onSubmit={handleSubmit}>
        <div>
          <label>Nhà cung cấp</label>
          <br />
          <select
            style={inputStyle}
            value={supplierId}
            onChange={(e) => setSupplierId(e.target.value)}
          >
            <option value="">-- Chọn nhà cung cấp --</option>
            {suppliers.map((supplier) => (
              <option key={supplier.id} value={supplier.id}>
                {supplier.code} - {supplier.name}
              </option>
            ))}
          </select>
        </div>

        <br />

        <div>
          <label>Kho nhận</label>
          <br />
          <select
            style={inputStyle}
            value={warehouseId}
            onChange={(e) => setWarehouseId(e.target.value)}
          >
            <option value="">-- Chọn kho nhận --</option>
            {warehouses.map((warehouse) => (
              <option key={warehouse.id} value={warehouse.id}>
                {warehouse.code} - {warehouse.name} ({warehouse.type})
              </option>
            ))}
          </select>
        </div>

        <hr />

        <h3>Thêm sản phẩm nhập</h3>

        <div>
          <label>Sản phẩm</label>
          <br />
          <select
            style={inputStyle}
            value={selectedProductId}
            onChange={(e) => setSelectedProductId(e.target.value)}
          >
            <option value="">-- Chọn sản phẩm --</option>
            {products.map((product) => (
              <option key={product.id} value={product.id}>
                {product.sku} - {product.name} - {product.unit}
              </option>
            ))}
          </select>
        </div>

        <br />

        <div>
          <label>Số lượng dự kiến</label>
          <br />
          <input
            style={inputStyle}
            type="number"
            min="1"
            value={expectedQuantity}
            onChange={(e) => setExpectedQuantity(e.target.value)}
          />
        </div>

        <br />

        <button type="button" className="primary-btn" onClick={handleAddItem}>
          + Thêm sản phẩm
        </button>

        <hr />

        <h3>Danh sách sản phẩm nhập</h3>

        <table border="1" cellPadding="10" cellSpacing="0" width="100%">
          <thead>
            <tr>
              <th>STT</th>
              <th>SKU</th>
              <th>Barcode</th>
              <th>Tên sản phẩm</th>
              <th>Đơn vị</th>
              <th>Số lượng dự kiến</th>
              <th>Thao tác</th>
            </tr>
          </thead>

          <tbody>
            {items.length === 0 ? (
              <tr>
                <td colSpan="7">Chưa có sản phẩm nào</td>
              </tr>
            ) : (
              items.map((item, index) => (
                <tr key={item.productId}>
                  <td>{index + 1}</td>
                  <td>{item.sku}</td>
                  <td>{item.barcode}</td>
                  <td>{item.productName}</td>
                  <td>{item.unit}</td>
                  <td>{item.expectedQuantity}</td>
                  <td>
                    <button type="button" onClick={() => handleRemoveItem(index)}>
                      Xóa
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>

        <br />

        <button type="submit" className="primary-btn">
          Tạo phiếu nhập hàng
        </button>
      </form>
    </div>
  );
}

const inputStyle = {
  width: "320px",
  padding: "10px",
  borderRadius: "8px",
  border: "1px solid #555",
  background: "#111827",
  color: "white",
};

export default CreateGoodsReceipt;