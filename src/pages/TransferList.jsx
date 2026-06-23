import { useEffect, useState } from "react";
import { getTransfers } from "../api/transferApi";
import { formatDateTime } from "../utils/formatDate";
import { getTransferStatusText } from "../utils/transferStatus";
import { useNavigate } from "react-router-dom";

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
    <div>
      <h1>Danh sách phiếu điều kho</h1>

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
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
              <td>{getTransferStatusText(item.status)}</td>
              <td>{formatDateTime(item.createdAt)}</td>
              <td>
                {item.completedAt
                  ? formatDateTime(item.completedAt)
                  : "Chưa hoàn tất"}
              </td>
              <td>
                <button
                  onClick={() => navigate(`/transfers/${item.id}`)}
                >
                  Xem chi tiết
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default TransferList;