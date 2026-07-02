import { NavLink } from "react-router-dom";
import {
    LayoutDashboard,
    ClipboardList,
    Package,
    Truck,
} from "lucide-react";

export default function Sidebar() {
    const menus = [
        {
            title: "Dashboard",
            path: "/warehouse",
            icon: <LayoutDashboard size={20} />,
            end: true,
        },
        {
            title: "Transfer Orders",
            path: "/warehouse/transfers",
            icon: <ClipboardList size={20} />,
        },
        {
            title: "Package Management",
            path: "/warehouse/packages",
            icon: <Package size={20} />,
        },
        {
            title: "Shipping Manifests",
            path: "/warehouse/manifests",
            icon: <Truck size={20} />,
        },
    ];

    return (
        <aside
            style={{
                width: "260px",
                minHeight: "100vh",
                background: "#f97316",
                color: "#fff",
                boxShadow: "0 0 10px rgba(0,0,0,0.1)",
            }}
        >
            <div
                style={{
                    padding: "24px",
                    borderBottom:
                        "1px solid rgba(255,255,255,0.2)",
                }}
            >
                <h1
                    style={{
                        margin: 0,
                        fontSize: "24px",
                        fontWeight: "700",
                    }}
                >
                    TOY STORAGE
                </h1>

                <p
                    style={{
                        marginTop: "6px",
                        color: "#ffedd5",
                        fontSize: "13px",
                    }}
                >
                    Warehouse Management System
                </p>
            </div>

            <nav
                style={{
                    padding: "12px",
                    display: "flex",
                    flexDirection: "column",
                    gap: "8px",
                }}
            >
                {menus.map((menu) => (
                    <NavLink
                        key={menu.path}
                        to={menu.path}
                        end={menu.end}
                        style={({ isActive }) => ({
                            display: "flex",
                            alignItems: "center",
                            gap: "12px",
                            padding: "12px 16px",
                            borderRadius: "10px",
                            textDecoration: "none",
                            color: isActive
                                ? "#f97316"
                                : "#ffffff",
                            background: isActive
                                ? "#ffffff"
                                : "transparent",
                            fontWeight: 600,
                            transition: "all 0.2s",
                        })}
                    >
                        {menu.icon}
                        <span>{menu.title}</span>
                    </NavLink>
                ))}
            </nav>
        </aside>
    );
}