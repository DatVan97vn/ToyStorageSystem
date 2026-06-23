import { useEffect, useState } from "react";
import { getProducts } from "../api/productApi";

function ProductPage() {
  const [products, setProducts] = useState([]);

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

  return (
    <div>
      <h1>Danh sách sản phẩm</h1>

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
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
          {products.length === 0 ? (
            <tr>
              <td colSpan="8">Không có dữ liệu sản phẩm</td>
            </tr>
          ) : (
            products.map((product) => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.sku}</td>
                <td>{product.name}</td>
                <td>{product.barcode}</td>
                <td>{product.unit}</td>
                <td>{product.costPrice}</td>
                <td>{product.salePrice}</td>
                <td>{product.weight}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ProductPage;