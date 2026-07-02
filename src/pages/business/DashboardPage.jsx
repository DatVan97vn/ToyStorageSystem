import { useEffect, useState } from "react";

import { getProducts } from "../../api/business/productApi";
import { getWarehouses } from "../../api/business/warehouseApi";
import { getSuppliers } from "../../api/business/supplierApi";
import { getTransfers } from "../../api/business/transferApi";
import { getInventory } from "../../api/business/inventoryApi";

function getDurationMinutes(createdAt, completedAt) {
  if (!createdAt) return 0;

  const start = new Date(createdAt);
  const end = completedAt ? new Date(completedAt) : new Date();
  const diffMs = end - start;

  if (diffMs < 0) return 0;

  return Math.floor(diffMs / 60000);
}

function formatDuration(minutes) {
  const hours = Math.floor(minutes / 60);
  const remainMinutes = minutes % 60;

  if (hours <= 0) return `${remainMinutes} phút`;
  return `${hours} giờ ${remainMinutes} phút`;
}

function isLateTransfer(item) {
  if (item.status === "COMPLETED") return false;
  if (!item.createdAt) return false;

  const minutes = getDurationMinutes(item.createdAt, item.completedAt);
  return minutes >= 240;
}

function DashboardPage() {
  const [stats, setStats] = useState({
    products: 0,
    warehouses: 0,
    suppliers: 0,
    transfers: 0,
    inventory: 0,

    draftTransfers: 0,
    approvedTransfers: 0,
    completedTransfers: 0,
    lateTransfers: 0,
    completionRate: 0,
  });

  const [longestTransfers, setLongestTransfers] = useState([]);
  const [lowStockItems, setLowStockItems] = useState([]);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        const [
          productsRes,
          warehousesRes,
          suppliersRes,
          transfersRes,
          inventoryRes,
        ] = await Promise.all([
          getProducts(),
          getWarehouses(),
          getSuppliers(),
          getTransfers(),
          getInventory(),
        ]);

        const transfers = Array.isArray(transfersRes.data)
          ? transfersRes.data
          : [];

        const inventory = Array.isArray(inventoryRes.data)
          ? inventoryRes.data
          : [];

        const completedTransfers = transfers.filter(
          (t) => t.status === "COMPLETED"
        ).length;

        const completionRate =
          transfers.length === 0
            ? 0
            : Math.round((completedTransfers / transfers.length) * 100);

        const topLongest = [...transfers]
          .map((item) => ({
            ...item,
            durationMinutes: getDurationMinutes(
              item.createdAt,
              item.completedAt
            ),
          }))
          .sort((a, b) => b.durationMinutes - a.durationMinutes)
          .slice(0, 5);

        const lowStock = inventory
          .filter((item) => Number(item.quantity) <= 10)
          .sort((a, b) => Number(a.quantity) - Number(b.quantity))
          .slice(0, 10);

        setStats({
          products: productsRes.data.length,
          warehouses: warehousesRes.data.length,
          suppliers: suppliersRes.data.length,
          transfers: transfers.length,
          inventory: inventory.length,

          draftTransfers: transfers.filter((t) => t.status === "DRAFT").length,
          approvedTransfers: transfers.filter((t) => t.status === "APPROVED")
            .length,
          completedTransfers,
          lateTransfers: transfers.filter((t) => isLateTransfer(t)).length,
          completionRate,
        });

        setLongestTransfers(topLongest);
        setLowStockItems(lowStock);
      } catch (error) {
        console.error("Lỗi tải dashboard:", error);
        alert("Không tải được dữ liệu dashboard");
      }
    };

    loadDashboard();
  }, []);

return (
  <div className="page-container">
    <div className="page-header">
      <div>
        <h1 className="page-title">Dashboard Business</h1>
        <p className="page-subtitle">
          Tổng quan nghiệp vụ kinh doanh trong hệ thống quản lý kho đồ chơi.
        </p>
      </div>
    </div>

    <div className="stat-grid">
      <div className="stat-card">
        <h3>Sản phẩm</h3>
        <h2>{stats.products}</h2>
      </div>

      <div className="stat-card">
        <h3>Kho / cửa hàng</h3>
        <h2>{stats.warehouses}</h2>
      </div>

      <div className="stat-card">
        <h3>Nhà cung cấp</h3>
        <h2>{stats.suppliers}</h2>
      </div>

      <div className="stat-card">
        <h3>Phiếu điều kho</h3>
        <h2>{stats.transfers}</h2>
      </div>

      <div className="stat-card">
        <h3>Dòng tồn kho</h3>
        <h2>{stats.inventory}</h2>
      </div>

      <div className="stat-card">
        <h3>Phiếu nháp</h3>
        <h2>{stats.draftTransfers}</h2>
      </div>

      <div className="stat-card">
        <h3>Phiếu đã duyệt</h3>
        <h2>{stats.approvedTransfers}</h2>
      </div>

      <div className="stat-card">
        <h3>Phiếu hoàn tất</h3>
        <h2>{stats.completedTransfers}</h2>
      </div>

      <div className="stat-card">
        <h3>Phiếu chậm xử lý</h3>
        <h2>{stats.lateTransfers}</h2>
      </div>

      <div className="stat-card">
        <h3>Tỷ lệ hoàn thành</h3>
        <h2>{stats.completionRate}%</h2>
      </div>
    </div>

    <section className="section-block">
      <h2 className="section-title">Top 5 phiếu xử lý lâu nhất</h2>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Mã phiếu</th>
              <th>Trạng thái</th>
              <th>Thời gian xử lý</th>
              <th>Cảnh báo</th>
            </tr>
          </thead>

          <tbody>
            {longestTransfers.length === 0 ? (
              <tr>
                <td colSpan="5">Chưa có dữ liệu phiếu điều kho</td>
              </tr>
            ) : (
              longestTransfers.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>{item.transferCode}</td>
                  <td>
                    <span className="status-badge">{item.status}</span>
                  </td>
                  <td>{formatDuration(item.durationMinutes)}</td>
                  <td>
                    {isLateTransfer(item)
                      ? "⚠ Chậm xử lý"
                      : "Đang theo dõi"}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </section>

    <section className="section-block">
      <h2 className="section-title">Cảnh báo tồn kho thấp</h2>

      <div className="table-wrapper">
        <table className="data-table">
          <thead>
            <tr>
              <th>ID tồn kho</th>
              <th>Sản phẩm</th>
              <th>Kho / cửa hàng</th>
              <th>Số lượng tồn</th>
              <th>Cảnh báo</th>
            </tr>
          </thead>

          <tbody>
            {lowStockItems.length === 0 ? (
              <tr>
                <td colSpan="5">Không có sản phẩm tồn kho thấp</td>
              </tr>
            ) : (
              lowStockItems.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>{item.productName}</td>
                  <td>{item.warehouseName}</td>
                  <td>{item.quantity}</td>
                  <td>⚠ Cần theo dõi / bổ sung hàng</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </section>
  </div>
);
}


export default DashboardPage;