import { BrowserRouter, Routes, Route } from "react-router-dom";
import TransferTracking from "./pages/TransferTracking";
import InventoryAllocation from "./pages/InventoryAllocation";
import ImportTransferExcel from "./pages/ImportTransferExcel";
import TransferDetail from "./pages/TransferDetail";
import GoodsReceiptList from "./pages/GoodsReceiptList";
import CreateGoodsReceipt from "./pages/CreateGoodsReceipt";
import GoodsReceiptDetail from "./pages/GoodsReceiptDetail";
import PackageList from "./pages/PackageList";

import MainLayout from "./layouts/MainLayout";

import DashboardPage from "./pages/DashboardPage";
import TransferList from "./pages/TransferList";
import CreateTransfer from "./pages/CreateTransfer";
import ProductPage from "./pages/ProductPage";
import WarehousePage from "./pages/WarehousePage";
import SupplierPage from "./pages/SupplierPage";
import InventoryPage from "./pages/InventoryPage";
import WarehouseScan from "./pages/WarehouseScan";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/" element={<DashboardPage />} />

          <Route path="/transfers" element={<TransferList />} />

          <Route path="/goods-receipts" element={<GoodsReceiptList />} />

          <Route
            path="/goods-receipts/create"
            element={<CreateGoodsReceipt />}
          />

          <Route
            path="/transfers/create"
            element={<CreateTransfer />}
          />

          <Route path="/products" element={<ProductPage />} />

          <Route path="/warehouses" element={<WarehousePage />} />

          <Route path="/suppliers" element={<SupplierPage />} />

          <Route path="/inventory" element={<InventoryPage />} />

          <Route path="/inventory/allocation" element={<InventoryAllocation />} />

          <Route path="/transfers/tracking" element={<TransferTracking />} />

          <Route path="/transfers/import-excel" element={<ImportTransferExcel />} />

          <Route path="/transfers/:id" element={<TransferDetail />} />

          <Route path="/goods-receipts/:id" element={<GoodsReceiptDetail />}  />

          <Route path="/packages" element={<PackageList />} />

          <Route path="/warehouse-scan" element={<WarehouseScan />}/>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;