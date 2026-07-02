import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getGoodsReceipts } from "../../api/business/goodsReceiptApi";

function GoodsReceiptList() {
  const [receipts, setReceipts] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadReceipts = async () => {
    try {
      setLoading(true);
      const res = await getGoodsReceipts();
      setReceipts(Array.isArray(res.data) ? res.data : []);
    } catch (error) {
      console.error("Lỗi tải phiếu nhập:", error);
      alert("Không tải được danh sách phiếu nhập hàng");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadReceipts();
  }, []);

  if (loading) return <p>Đang tải dữ liệu...</p>;

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h2>Phiếu nhập hàng</h2>
          <p>Danh sách phiếu nhập hàng từ nhà cung cấp vào kho.</p>
        </div>

        <button
          className="primary-btn"
          onClick={() => navigate("/business/goods-receipts/create")}
        >
          + Tạo phiếu nhập
        </button>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Mã phiếu</th>
              <th>Kho nhận</th>
              <th>Trạng thái</th>
              <th>Ngày tạo</th>
              <th>Ngày hoàn tất</th>
              <th>Thao tác</th>
            </tr>
          </thead>

          <tbody>
            {receipts.length === 0 ? (
              <tr>
                <td colSpan="7" className="empty-cell">
                  Chưa có phiếu nhập hàng
                </td>
              </tr>
            ) : (
              receipts.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>{item.receiptCode}</td>
                  <td>{item.warehouseName || "Chưa cập nhật"}</td>
                  <td>
                    <span className="status-badge draft">{item.status}</span>
                  </td>
                  <td>{item.createdAt || "-"}</td>
                  <td>{item.completedReceiveAt || "Chưa hoàn tất"}</td>
                  <td>
                    <button
                      className="primary-btn"
                      onClick={() => navigate(`/business/goods-receipts/${item.id}`)}
                    >
                      Xem
                    </button>
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

export default GoodsReceiptList;