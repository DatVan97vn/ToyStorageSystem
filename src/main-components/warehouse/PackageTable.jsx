import { useMemo, useState } from "react";
import Table from "../../components/warehouse/Table";
import StatusBadge from "../../components/warehouse/StatusBadge";
import Button from "../../components/warehouse/Button";

import "../../assets/css/warehouse/PackageTable.css";

export default function PackageTable({
                                         packages = [],
                                         onView,
                                         onClose,
                                         onPrint,
                                     }) {
    const [keyword, setKeyword] = useState("");

    const filteredPackages = useMemo(() => {
        const value = keyword.trim().toLowerCase();

        if (!value) return packages;

        return packages.filter((row) =>
            [
                row.packageCode,
                row.boxCode,
                row.transferCode,
                row.fromWarehouseName,
                row.toWarehouseName,
                row.status,
            ]
                .filter(Boolean)
                .some((item) =>
                    String(item).toLowerCase().includes(value)
                )
        );
    }, [packages, keyword]);

    const columns = [
        {
            key: "packageCode",
            title: "Package Code",
            render: (row) => row.packageCode || row.boxCode || "-",
        },
        {
            key: "transferCode",
            title: "Transfer Order",
            render: (row) => row.transferCode || "-",
        },
        {
            key: "status",
            title: "Status",
            render: (row) => <StatusBadge status={row.status} />,
        },
        {
            key: "actions",
            title: "Actions",
            render: (row) => (
                <div className="package-action-group">
                    <Button
                        type="button"
                        variant="outline"
                        className="package-btn package-btn-outline"
                        onClick={() => onView?.(row)}
                    >
                        View Details
                    </Button>

                    {row.status === "OPEN" && (
                        <Button
                            type="button"
                            className="package-btn package-btn-filled"
                            onClick={() => onClose?.(row)}
                        >
                            Seal & Print
                        </Button>
                    )}

                    {row.status === "SEALED" && (
                        <Button
                            type="button"
                            className="package-btn package-btn-filled"
                            onClick={() => onPrint?.(row)}
                        >
                            Print Label
                        </Button>
                    )}
                </div>
            ),
        },
    ];

    return (
        <div>
            <div className="package-table-toolbar">
                <input
                    className="package-search-input"
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                    placeholder="Search by package code, transfer order, warehouse, or status..."
                />
            </div>

            <Table
                columns={columns}
                data={filteredPackages}
                emptyText="No Packages Found"
            />
        </div>
    );
}