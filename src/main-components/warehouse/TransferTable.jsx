import Table from "../../components/warehouse/Table";
import StatusBadge from "../../components/warehouse/StatusBadge";
import Button from "../../components/warehouse/Button";

import "../../assets/css/warehouse/TransferTable.css";

export default function TransferTable({
                                          transfers = [],
                                          onView,
                                          onCheck,
                                      }) {
    const columns = [
        {
            key: "transferCode",
            title: "Transfer Code",
        },
        {
            key: "fromWarehouse",
            title: "Source Warehouse",
            render: (row) =>
                row.fromWarehouse?.name || "-",
        },
        {
            key: "toWarehouse",
            title: "Destination Warehouse",
            render: (row) =>
                row.toWarehouse?.name || "-",
        },
        {
            key: "status",
            title: "Status",
            render: (row) => (
                <StatusBadge status={row.status} />
            ),
        },
        {
            key: "actions",
            title: "Actions",
            render: (row) => (
                <div className="transfer-action-group">

                    <Button
                        className="transfer-btn transfer-btn-outline"
                        onClick={() => onView?.(row)}
                    >
                        View Details
                    </Button>

                    <Button
                        className="transfer-btn transfer-btn-filled"
                        onClick={() => onCheck?.(row)}
                    >
                        Check Items
                    </Button>

                </div>
            ),
        },
    ];

    return (
        <Table
            columns={columns}
            data={transfers}
            emptyText="No Transfer Orders Found"
        />
    );
}