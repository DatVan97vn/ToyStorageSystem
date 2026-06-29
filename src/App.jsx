import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Layout from "./components/warehouse/Layout";
import ProtectedRoute from "./main-components/two-factor/ProtectedRoute";

import LoginPage from "./pages/auth/LoginPage";
import SetupTwoFactorPage from "./pages/two-factor/SetupTwoFactorPage";
import VerifyOtpPage from "./pages/two-factor/VerifyOtpPage";

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
import ChangePasswordPage from "./pages/auth/ChangePasswordPage";

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />

          <Route path="/login" element={<LoginPage />} />
            <Route path="/change-password" element={<ChangePasswordPage />} />
          <Route path="/setup-2fa" element={<SetupTwoFactorPage />} />
          <Route path="/verify-otp" element={<VerifyOtpPage />} />

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
        </Routes>
      </BrowserRouter>
  );
}

export default App;