import { useEffect, useMemo, useState } from "react";
import { getProducts } from "../../api/business/productApi";

function ProductPage() {
  const [products, setProducts] = useState([]);
  const [keyword, setKeyword] = useState("");

  useEffect(() => {
    const loadProducts = async () => {
      try {
        const res = await getProducts();
        setProducts(res.data);
      } catch (error) {
        console.error("Lỗi lấy danh sách sản phẩm:", error);
      }
    };

    loadProducts();
  }, []);

  const filteredProducts = useMemo(() => {
    const text = keyword.trim().toLowerCase();

    if (!text) return products;

    return products.filter((product) =>
      [
        product.id,
        product.sku,
        product.name,
        product.barcode,
        product.unit,
        product.importPrice,
        product.salePrice,
        product.weight,
      ]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(text))
    );
  }, [keyword, products]);

  const formatMoney = (value) => {
    if (value === null || value === undefined || value === "") return "-";

    return Number(value).toLocaleString("vi-VN") + " đ";
  };

  return (
    <div className="page-container master-page">
      <div className="page-header">
        <div>
          <h1>Sản phẩm</h1>
          <p>Quản lý danh sách sản phẩm, SKU, barcode, đơn vị tính và giá bán.</p>
        </div>
      </div>

      <div className="stat-grid master-stat-grid">
        <div className="stat-card">
          <span>Tổng sản phẩm</span>
          <strong>{products.length}</strong>
        </div>

        <div className="stat-card">
          <span>Có barcode</span>
          <strong>{products.filter((item) => item.barcode).length}</strong>
        </div>

        <div className="stat-card">
          <span>Đơn vị khác nhau</span>
          <strong>{new Set(products.map((item) => item.unit)).size}</strong>
        </div>
      </div>

      <div className="filter-bar">
        <input
          value={keyword}
          onChange={(event) => setKeyword(event.target.value)}
          placeholder="Tìm theo tên sản phẩm, SKU, barcode, đơn vị..."
        />
      </div>

      <div className="table-wrapper master-table-wrapper">
        <table className="data-table master-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>SKU</th>
              <th>Tên sản phẩm</th>
              <th>Barcode</th>
              <th>Đơn vị</th>
              <th>Giá nhập</th>
              <th>Giá bán</th>
              <th>Khối lượng</th>
            </tr>
          </thead>

          <tbody>
            {filteredProducts.length === 0 ? (
              <tr>
                <td className="empty-cell" colSpan="8">
                  Không có dữ liệu sản phẩm
                </td>
              </tr>
            ) : (
              filteredProducts.map((product) => (
                <tr key={product.id}>
                  <td>{product.id}</td>
                  <td className="code-cell">{product.sku}</td>
                  <td className="strong-cell">{product.name}</td>
                  <td>{product.barcode || "-"}</td>
                  <td>
                    <span className="unit-badge">{product.unit || "-"}</span>
                  </td>
                  <td>{formatMoney(product.importPrice)}</td>
                  <td>{formatMoney(product.salePrice)}</td>
                  <td>{product.weight ? `${product.weight} kg` : "-"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default ProductPage;