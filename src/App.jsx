import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Layout from "./components/warehouse/Layout";
import ProtectedRoute from "./main-components/two-factor/ProtectedRoute";

import LoginPage from "./pages/auth/LoginPage";
import SetupTwoFactorPage from "./pages/two-factor/SetupTwoFactorPage";
import VerifyOtpPage from "./pages/two-factor/VerifyOtpPage";
import ChangePasswordPage from "./pages/auth/ChangePasswordPage";

import WarehouseDashboard from "./pages/warehouse/WarehouseDashboard";
import TransferList from "./pages/warehouse/TransferList";
import TransferDetail from "./pages/warehouse/TransferDetail";
import TransferCheck from "./pages/warehouse/TransferCheck";
import PackageList from "./pages/warehouse/PackageList";
import ManifestList from "./pages/warehouse/ManifestList";
import ManifestDetail from "./pages/warehouse/ManifestDetail";
import PackageBarcodePrint from "./pages/warehouse/PackageBarcodePrint";
import TransferPackages from "./pages/warehouse/TransferPackages";
import PackageDetail from "./pages/warehouse/PackageDetail";
import BusinessLayout from "./layouts/business/MainLayout";

import BusinessDashboard from "./pages/business/DashboardPage";
import BusinessTransferList from "./pages/business/TransferList";
import BusinessCreateTransfer from "./pages/business/CreateTransfer";
import BusinessTransferDetail from "./pages/business/TransferDetail";
import BusinessGoodsReceiptList from "./pages/business/GoodsReceiptList";
import BusinessCreateGoodsReceipt from "./pages/business/CreateGoodsReceipt";
import BusinessGoodsReceiptDetail from "./pages/business/GoodsReceiptDetail";
import BusinessProductPage from "./pages/business/ProductPage";
import BusinessWarehousePage from "./pages/business/WarehousePage";
import BusinessSupplierPage from "./pages/business/SupplierPage";
import BusinessInventoryPage from "./pages/business/InventoryPage";
import BusinessInventoryAllocation from "./pages/business/InventoryAllocation";
import BusinessTransferTracking from "./pages/business/TransferTracking";
import BusinessImportTransferExcel from "./pages/business/ImportTransferExcel";


function App() {
  return (
      <BrowserRouter>
        <Routes>

          <Route path="/" element={<Navigate to="/login" replace />} />

          <Route path="/login" element={<LoginPage />} />
          <Route path="/change-password" element={<ChangePasswordPage />} />
          <Route path="/setup-2fa" element={<SetupTwoFactorPage />} />
          <Route path="/verify-otp" element={<VerifyOtpPage />} />

          {/* ================== Warehouse ================== */}
          <Route
            element={
              <ProtectedRoute>
                <Layout />
              </ProtectedRoute>
            }
          >
            <Route path="/warehouse" element={<WarehouseDashboard />} />
            <Route path="/warehouse/transfers" element={<TransferList />} />
            <Route path="/warehouse/packages/:id/barcode" element={<PackageBarcodePrint />} />
            <Route path="/warehouse/transfers/:id/packages" element={<TransferPackages />} />
            <Route path="/warehouse/transfers/:id" element={<TransferDetail />} />
            <Route path="/warehouse/manifests/:id" element={<ManifestDetail />} />
            <Route path="/warehouse/packages/:id" element={<PackageDetail />} />
            <Route path="/warehouse/transfers/:id/check" element={<TransferCheck />} />
            <Route path="/warehouse/packages" element={<PackageList />} />
            <Route path="/warehouse/manifests" element={<ManifestList />} />
          </Route>

          {/* ================== Business ================== */}
          <Route
            element={
              <ProtectedRoute>
                <BusinessLayout />
              </ProtectedRoute>
            }
          >
            <Route path="/business" element={<BusinessDashboard />} />

            <Route path="/business/transfers" element={<BusinessTransferList />} />
            <Route path="/business/transfers/create" element={<BusinessCreateTransfer />} />
            <Route path="/business/transfers/tracking" element={<BusinessTransferTracking />} />
            <Route path="/business/transfers/import-excel" element={<BusinessImportTransferExcel />} />
            <Route path="/business/transfers/:id" element={<BusinessTransferDetail />} />

            <Route path="/business/goods-receipts" element={<BusinessGoodsReceiptList />} />
            <Route path="/business/goods-receipts/create" element={<BusinessCreateGoodsReceipt />} />
            <Route path="/business/goods-receipts/:id" element={<BusinessGoodsReceiptDetail />} />

            <Route path="/business/products" element={<BusinessProductPage />} />
            <Route path="/business/warehouses" element={<BusinessWarehousePage />} />
            <Route path="/business/suppliers" element={<BusinessSupplierPage />} />
            <Route path="/business/inventory" element={<BusinessInventoryPage />} />
            <Route path="/business/inventory/allocation" element={<BusinessInventoryAllocation />} />
          </Route>

        </Routes>
      </BrowserRouter>
  );
}

export default App;