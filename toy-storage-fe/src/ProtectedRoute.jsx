import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import { checkLogin } from "./services/authService";

function ProtectedRoute({ children }) {

  const [status, setStatus] = useState("checking");

  useEffect(() => {

    const verify = async () => {

      try {

        const res = await checkLogin();

        if (res.data === "LOGIN") {
          setStatus("ok");
        } else {
          setStatus("not-login");
        }

      } catch {
        setStatus("not-login");
      }

    };

    verify();

  }, []);

  if (status === "checking") {
    return <p>Đang kiểm tra đăng nhập...</p>;
  }

  if (status === "not-login") {
    return <Navigate to="/" replace />;
  }

  return children;
}

export default ProtectedRoute;