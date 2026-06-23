import { Outlet } from "react-router-dom";
import Sidebar from "../components/Sidebar";

function MainLayout() {
  return (
    <div style={layoutStyle}>
      <Sidebar />

      <main style={contentStyle}>
        <Outlet />
      </main>
    </div>
  );
}

const layoutStyle = {
  display: "flex",
  minHeight: "100vh",
  background: "#0f1117",
};

const contentStyle = {
  flex: 1,
  padding: "36px 48px",
  overflowX: "auto",
};

export default MainLayout;