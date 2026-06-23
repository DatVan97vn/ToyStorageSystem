import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/api";

const STATUS_OPTIONS = ["ALL", "DRAFT", "APPROVED", "COMPLETED"];

function formatDate(value) {
  if (!value) return "-";
  return new Date(value).toLocaleString("vi-VN");
}

function getStatusClass(status) {
  switch (status) {
    case "DRAFT":
      return "status-badge draft";
    case "APPROVED":
      return "status-badge approved";
    case "COMPLETED":
      return "status-badge completed";
    default:
      return "status-badge";
  }
}

function getDuration(createdAt, completedAt) {
  if (!createdAt) return "-";

  const start = new Date(createdAt);
  const end = completedAt ? new Date(completedAt) : new Date();
  const diffMs = end - start;

  if (diffMs < 0) return "-";

  const minutes = Math.floor(diffMs / 60000);
  const hours = Math.floor(minutes / 60);
  const remainMinutes = minutes % 60;

  if (hours <= 0) return `${remainMinutes} phút`;
  return `${hours} giờ ${remainMinutes} phút`;
}

function getProcessLabel(status) {
  switch (status) {
    case "DRAFT":
      return "Chờ duyệt";
    case "APPROVED":
      return "Kho đang xử lý";
    case "COMPLETED":
      return "Đã hoàn thành";
    default:
      return "Không xác định";
  }
}

function isLateTransfer(item) {
  if (item.status === "COMPLETED") return false;
  if (!item.createdAt) return false;

  const createdTime = new Date(item.createdAt).getTime();
  const now = new Date().getTime();
  const diffHours = (now - createdTime) / (1000 * 60 * 60);

  return diffHours >= 4;
}

function getWarningText(item) {
  if (isLateTransfer(item)) {
    return "⚠ Chậm xử lý";
  }

  if (item.status === "COMPLETED") {
    return "Đúng quy trình";
  }

  return "Đang theo dõi";
}

export default function TransferTracking() {
  const [transfers, setTransfers] = useState([]);
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [searchText, setSearchText] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const fetchTransfers = async () => {
    try {
      setLoading(true);
      const res = await api.get("/transfers");
      setTransfers(Array.isArray(res.data) ? res.data : []);
    } catch (error) {
      console.error("Failed to fetch transfers:", error);
      alert("Không thể tải danh sách phiếu điều kho");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTransfers();
  }, []);

  const filteredTransfers = useMemo(() => {
    return transfers.filter((item) => {
      const matchStatus =
        statusFilter === "ALL" || item.status === statusFilter;

      const keyword = searchText.trim().toLowerCase();

      let matchSearch = true;

      if (keyword) {
        const isNumberOnly = /^\d+$/.test(keyword);

        if (isNumberOnly) {
          matchSearch = String(item.id) === keyword;
        } else {
          matchSearch = item.transferCode?.toLowerCase().includes(keyword);
        }
      }

      return matchStatus && matchSearch;
    });
  }, [transfers, statusFilter, searchText]);

  const total = transfers.length;
  const draftCount = transfers.filter((x) => x.status === "DRAFT").length;
  const approvedCount = transfers.filter((x) => x.status === "APPROVED").length;
  const completedCount = transfers.filter((x) => x.status === "COMPLETED").length;

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h2>Theo dõi phiếu điều kho</h2>
          <p>Business theo dõi trạng thái xử lý các phiếu nhập/xuất điều kho.</p>
        </div>

        <button className="primary-btn" onClick={fetchTransfers}>
          Làm mới
        </button>
      </div>

      <div className="stat-grid">
        <div className="stat-card">
          <span>Tổng phiếu</span>
          <strong>{total}</strong>
        </div>

        <div className="stat-card">
          <span>Nháp</span>
          <strong>{draftCount}</strong>
        </div>

        <div className="stat-card">
          <span>Đã duyệt</span>
          <strong>{approvedCount}</strong>
        </div>

        <div className="stat-card">
          <span>Hoàn tất</span>
          <strong>{completedCount}</strong>
        </div>
      </div>

      <div className="filter-bar">
        <input
          type="text"
          placeholder="Tìm theo mã phiếu hoặc ID..."
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />

        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          {STATUS_OPTIONS.map((status) => (
            <option key={status} value={status}>
              {status === "ALL" ? "Tất cả trạng thái" : status}
            </option>
          ))}
        </select>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Mã phiếu</th>
              <th>Kho xuất</th>
              <th>Kho nhập</th>
              <th>Trạng thái</th>
              <th>Ngày tạo</th>
              <th>Ngày hoàn tất</th>
              <th>Thời gian xử lý</th>
              <th>Tình trạng xử lý</th>
              <th>Cảnh báo</th>
              <th>Thao tác</th>
            </tr>
          </thead>

          <tbody>
            {loading ? (
              <tr>
                <td colSpan="11" className="empty-cell">
                  Đang tải dữ liệu...
                </td>
              </tr>
            ) : filteredTransfers.length === 0 ? (
              <tr>
                <td colSpan="11" className="empty-cell">
                  Không có phiếu phù hợp
                </td>
              </tr>
            ) : (
              filteredTransfers.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>{item.transferCode}</td>
                  <td>
                    {item.fromWarehouse?.warehouseName ||
                      item.fromWarehouse?.name ||
                      "Chưa cập nhật"}
                  </td>
                  <td>
                    {item.toWarehouse?.warehouseName ||
                      item.toWarehouse?.name ||
                      "Chưa cập nhật"}
                  </td>
                  <td>
                    <span className={getStatusClass(item.status)}>
                      {item.status}
                    </span>
                  </td>
                  <td>{formatDate(item.createdAt)}</td>
                  <td>{formatDate(item.completedAt)}</td>
                  <td>{getDuration(item.createdAt, item.completedAt)}</td>
                  <td>{getProcessLabel(item.status)}</td>
                  <td>
                    <span
                      className={
                        isLateTransfer(item)
                          ? "warning-badge late"
                          : "warning-badge"
                      }
                    >
                      {getWarningText(item)}
                    </span>
                  </td>
                  <td>
                    <button
                      type="button"
                      onClick={() => navigate(`/transfers/${item.id}`)}
                      className="primary-btn"
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