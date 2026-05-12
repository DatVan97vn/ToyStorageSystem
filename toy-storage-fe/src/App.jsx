import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import ChangePassword from "./pages/ChangePassword";
import Setup2FA from "./pages/Setup2FA";
import OTP from "./pages/OTP";
import Home from "./pages/Home";
import ProtectedRoute from "./ProtectedRoute";
import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />

        <Route path="/change-password" element={<ChangePassword />} />

        <Route path="/setup-2fa" element={<Setup2FA />} />

        <Route path="/otp" element={<OTP />} />

        <Route
          path="/home"
          element={
            <ProtectedRoute>
              <Home />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;