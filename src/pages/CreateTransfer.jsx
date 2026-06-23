import { useEffect, useState } from "react";
import { createTransfer } from "../api/transferApi";
import { getWarehouses } from "../api/warehouseApi";
import { getProducts } from "../api/productApi";
import { getInventoryByWarehouse } from "../api/inventoryApi";

function CreateTransfer() {
  const [warehouses, setWarehouses] = useState([]);
  const [products, setProducts] = useState([]);
  const [inventory, setInventory] = useState([]);

  const [fromWarehouseId, setFromWarehouseId] = useState("");
  const [toWarehouseId, setToWarehouseId] = useState("");

  const [selectedProductId, setSelectedProductId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [items, setItems] = useState([]);

  useEffect(() => {
    const loadData = async () => {
      const warehouseRes = await getWarehouses();
      const productRes = await getProducts();

      setWarehouses(warehouseRes.data);
      setProducts(productRes.data);
    };

    loadData();
  }, []);

  useEffect(() => {
    const loadInventory = async () => {
      if (!fromWarehouseId) {
        setInventory([]);
        setItems([]);
        setSelectedProductId("");
        return;
      }

      try {
        const res = await getInventoryByWarehouse(fromWarehouseId);
        setInventory(res.data);
        setItems([]);
        setSelectedProductId("");
        setQuantity(1);
      } catch (error) {
        console.error("Lỗi load tồn kho:", error);
        alert("Không tải được tồn kho của kho xuất");
        setInventory([]);
      }
    };

    loadInventory();
  }, [fromWarehouseId]);

  const getAvailableQuantity = (productId) => {
    const inventoryItem = inventory.find(
      (item) => item.productId === Number(productId)
    );

    return inventoryItem ? Number(inventoryItem.quantity) : 0;
  };

  const getUsedQuantity = (productId) => {
    return items
      .filter((item) => item.productId === Number(productId))
      .reduce((sum, item) => sum + Number(item.quantity), 0);
  };

  const handleAddItem = () => {
    if (!fromWarehouseId) {
      alert("Vui lòng chọn kho xuất trước");
      return;
    }

    if (!selectedProductId || Number(quantity) <= 0) {
      alert("Vui lòng chọn sản phẩm và nhập số lượng hợp lệ");
      return;
    }

    const product = products.find((p) => p.id === Number(selectedProductId));

    if (!product) {
      alert("Không tìm thấy sản phẩm");
      return;
    }

    const availableQty = getAvailableQuantity(selectedProductId);
    const usedQty = getUsedQuantity(selectedProductId);
    const remainingQty = availableQty - usedQty;
    const inputQty = Number(quantity);

    if (availableQty <= 0) {
      alert("Sản phẩm này không có tồn kho trong kho xuất");
      return;
    }

    if (inputQty > remainingQty) {
      alert(
        `Số lượng vượt tồn kho. ${product.name} còn có thể thêm ${remainingQty} sản phẩm.`
      );
      return;
    }

    const existedIndex = items.findIndex(
      (item) => item.productId === Number(selectedProductId)
    );

    if (existedIndex !== -1) {
      const updatedItems = [...items];
      updatedItems[existedIndex].quantity += inputQty;
      setItems(updatedItems);
    } else {
      const newItem = {
        productId: product.id,
        productName: product.name,
        sku: product.sku,
        barcode: product.barcode,
        unit: product.unit,
        quantity: inputQty,
        availableQty,
      };

      setItems([...items, newItem]);
    }

    setSelectedProductId("");
    setQuantity(1);
  };

  const handleRemoveItem = (index) => {
    const newItems = items.filter((_, i) => i !== index);
    setItems(newItems);
  };

  const validateStockBeforeSubmit = () => {
    for (const item of items) {
      const availableQty = getAvailableQuantity(item.productId);

      if (item.quantity > availableQty) {
        alert(
          `Sản phẩm ${item.productName} vượt tồn kho. Tồn hiện tại: ${availableQty}, số lượng tạo phiếu: ${item.quantity}.`
        );
        return false;
      }
    }

    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!fromWarehouseId || !toWarehouseId) {
      alert("Vui lòng chọn kho xuất và kho nhập");
      return;
    }

    if (fromWarehouseId === toWarehouseId) {
      alert("Kho xuất và kho nhập không được trùng nhau");
      return;
    }

    if (items.length === 0) {
      alert("Vui lòng thêm ít nhất một sản phẩm");
      return;
    }

    if (!validateStockBeforeSubmit()) {
      return;
    }

    const payload = {
      fromWarehouseId: Number(fromWarehouseId),
      toWarehouseId: Number(toWarehouseId),
      items: items.map((item) => ({
        productId: item.productId,
        quantity: item.quantity,
      })),
    };

    try {
      console.log("PAYLOAD GỬI LÊN:", payload);

      const res = await createTransfer(payload);
      console.log("Tạo phiếu thành công:", res.data);
      alert("Tạo phiếu điều kho thành công");

      setFromWarehouseId("");
      setToWarehouseId("");
      setItems([]);
      setInventory([]);
      setSelectedProductId("");
      setQuantity(1);
    } catch (error) {
      console.error("Lỗi tạo phiếu:", error);
      alert("Tạo phiếu thất bại");
    }
  };

  const availableProducts = inventory
    .filter((item) => Number(item.quantity) > 0)
    .map((item) => {
      const product = products.find((p) => p.id === item.productId);

      return {
        ...product,
        inventoryQuantity: item.quantity,
      };
    })
    .filter((product) => product.id);

  return (
    <div
      style={{
        maxWidth: "1200px",
        margin: "0 auto",
      }}
    >
      <h1>Tạo phiếu điều kho</h1>

      <form onSubmit={handleSubmit}>
        <div>
          <label>Kho xuất</label>
          <br />
          <select
            style={inputStyle}
            value={fromWarehouseId}
            onChange={(e) => setFromWarehouseId(e.target.value)}
          >
            <option value="">-- Chọn kho xuất --</option>
            {warehouses.map((warehouse) => (
              <option key={warehouse.id} value={warehouse.id}>
                {warehouse.code} - {warehouse.name} ({warehouse.type})
              </option>
            ))}
          </select>
        </div>

        <br />

        <div>
          <label>Kho nhập</label>
          <br />
          <select
            style={inputStyle}
            value={toWarehouseId}
            onChange={(e) => setToWarehouseId(e.target.value)}
          >
            <option value="">-- Chọn kho nhập --</option>
            {warehouses
              .filter((warehouse) => String(warehouse.id) !== String(fromWarehouseId))
              .map((warehouse) => (
                <option key={warehouse.id} value={warehouse.id}>
                  {warehouse.code} - {warehouse.name} ({warehouse.type})
                </option>
              ))}
          </select>
        </div>

        <hr />

        <h3>Thêm sản phẩm</h3>

        {!fromWarehouseId && (
          <p style={{ color: "orange" }}>Vui lòng chọn kho xuất để tải tồn kho.</p>
        )}

        {fromWarehouseId && inventory.length === 0 && (
          <p style={{ color: "orange" }}>Kho xuất hiện chưa có sản phẩm tồn kho.</p>
        )}

        <div>
          <label>Sản phẩm</label>
          <br />
          <select
            style={inputStyle}
            value={selectedProductId}
            onChange={(e) => setSelectedProductId(e.target.value)}
            disabled={!fromWarehouseId || availableProducts.length === 0}
          >
            <option value="">-- Chọn sản phẩm --</option>
            {availableProducts.map((product) => {
              const usedQty = getUsedQuantity(product.id);
              const remainingQty = product.inventoryQuantity - usedQty;

              return (
                <option
                  key={product.id}
                  value={product.id}
                  disabled={remainingQty <= 0}
                >
                  {product.sku} - {product.name} - Tồn còn lại: {remainingQty}
                </option>
              );
            })}
          </select>
        </div>

        <br />

        <div>
          <label>Số lượng</label>
          <br />
          <input
            style={inputStyle}
            type="number"
            min="1"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
          />
        </div>

        <br />

        <button type="button" onClick={handleAddItem}>
          + Thêm sản phẩm
        </button>

        <hr />

        <h3>Danh sách sản phẩm trong phiếu</h3>

        <table border="1" cellPadding="10" cellSpacing="0" width="100%">
          <thead>
            <tr>
              <th>STT</th>
              <th>SKU</th>
              <th>Barcode</th>
              <th>Tên sản phẩm</th>
              <th>Đơn vị</th>
              <th>Tồn kho</th>
              <th>Số lượng chuyển</th>
              <th>Thao tác</th>
            </tr>
          </thead>

          <tbody>
            {items.length === 0 ? (
              <tr>
                <td colSpan="8">Chưa có sản phẩm nào</td>
              </tr>
            ) : (
              items.map((item, index) => (
                <tr key={index}>
                  <td>{index + 1}</td>
                  <td>{item.sku}</td>
                  <td>{item.barcode}</td>
                  <td>{item.productName}</td>
                  <td>{item.unit}</td>
                  <td>{getAvailableQuantity(item.productId)}</td>
                  <td>{item.quantity}</td>
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

        <button type="submit">Tạo phiếu điều kho</button>
      </form>
    </div>
  );
}

const inputStyle = {
  width: "300px",
  padding: "10px",
  borderRadius: "8px",
  border: "1px solid #555",
  background: "#111827",
  color: "white",
};

export default CreateTransfer;