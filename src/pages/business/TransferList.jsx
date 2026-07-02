import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getTransfers } from "../../api/business/transferApi";
import { formatDateTime } from "../../utils/formatDate";
import { getTransferStatusText } from "../../utils/transferStatus";

function TransferList() {
  const [transfers, setTransfers] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTransfers = async () => {
      try {
        const res = await getTransfers();
        setTransfers(res.data);
      } catch (error) {
        console.error("Lỗi lấy danh sách phiếu:", error);
        alert("Không lấy được danh sách phiếu điều kho");
      } finally {
        setLoading(false);
      }
    };

    fetchTransfers();
  }, []);

  if (loading) {
    return <p>Đang tải dữ liệu...</p>;
  }

return (
  <div className="page-container">
    <div className="page-header">
      <div>
        <h1 className="page-title">Danh sách phiếu điều kho</h1>
        <p className="page-subtitle">Theo dõi các phiếu điều kho trong hệ thống</p>
      </div>

      <button
        className="primary-btn"
        onClick={() => navigate("/business/transfers/create")}
      >
        + Tạo phiếu
      </button>
    </div>

    <div className="table-wrapper">
      <table className="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Mã phiếu</th>
            <th>Trạng thái</th>
            <th>Ngày tạo</th>
            <th>Ngày hoàn tất</th>
            <th>Thao tác</th>
          </tr>
        </thead>

        <tbody>
          {transfers.map((item) => (
            <tr key={item.id}>
              <td>{item.id}</td>
              <td>{item.transferCode}</td>
              <td>
                <span className="status-badge">
                  {getTransferStatusText(item.status)}
                </span>
              </td>
              <td>{formatDateTime(item.createdAt)}</td>
              <td>
                {item.completedAt
                  ? formatDateTime(item.completedAt)
                  : "Chưa hoàn tất"}
              </td>
              <td>
                <button
                  className="outline-btn"
                  onClick={() => navigate(`/business/transfers/${item.id}`)}
                >
                  Xem chi tiết
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);
}

export default TransferList;