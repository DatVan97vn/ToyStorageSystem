import { useEffect, useState } from "react";

import { getPackages } from "../../api/business/packageApi";
function PackageList() {
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadPackages = async () => {
    try {
      setLoading(true);
      const res = await getPackages();
      setPackages(Array.isArray(res.data) ? res.data : []);
    } catch (error) {
      console.error("Lỗi tải danh sách thùng kiện:", error);
      alert("Không tải được danh sách thùng kiện");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPackages();
  }, []);

  if (loading) return <p>Đang tải dữ liệu...</p>;

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h2>Thùng kiện</h2>
          <p>
            Theo dõi các thùng/kiện được Warehouse đóng từ phiếu điều kho.
          </p>
        </div>

        <button className="primary-btn" onClick={loadPackages}>
          Làm mới
        </button>
      </div>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Mã thùng/kiện</th>
              <th>Trạng thái</th>
              <th>Thời gian đóng</th>
              <th>Ghi chú nghiệp vụ</th>
            </tr>
          </thead>

          <tbody>
            {packages.length === 0 ? (
              <tr>
                <td colSpan="5" className="empty-cell">
                  Chưa có thùng kiện
                </td>
              </tr>
            ) : (
              packages.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>{item.packageCode}</td>
                  <td>{item.status}</td>
                  <td>{item.packedAt || "Chưa cập nhật"}</td>
                  <td>
                    1 thùng có thể chứa hàng từ nhiều phiếu điều kho
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

export default PackageList;