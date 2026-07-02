import StatusBadge from "../../components/warehouse/StatusBadge";
import "../../assets/css/warehouse/WarehousePages.css";

export default function TransferInfoCard({ transfer }) {
    if (!transfer) {
        return null;
    }

    return (
        <div className="warehouse-card">
            <div className="warehouse-info-header">
                <h2 className="warehouse-card-title">
                    Transfer Information
                </h2>

                <StatusBadge status={transfer.status} />
            </div>

            <div className="warehouse-info-grid">
                <InfoItem
                    label="Transfer Code"
                    value={transfer.transferCode}
                />

                <InfoItem
                    label="Source Warehouse"
                    value={transfer.fromWarehouse?.name}
                />

                <InfoItem
                    label="Source Warehouse Code"
                    value={transfer.fromWarehouse?.code}
                />

                <InfoItem
                    label="Destination Warehouse"
                    value={transfer.toWarehouse?.name}
                />

                <InfoItem
                    label="Destination Warehouse Code"
                    value={transfer.toWarehouse?.code}
                />

                <InfoItem
                    label="Created Date"
                    value={
                        transfer.createdAt
                            ? new Date(
                                transfer.createdAt
                            ).toLocaleString("en-US")
                            : "-"
                    }
                />

                <InfoItem
                    label="Notes"
                    value={transfer.note || "-"}
                />
            </div>
        </div>
    );
}

function InfoItem({ label, value }) {
    return (
        <div className="warehouse-info-item">
            <p className="warehouse-info-label">
                {label}
            </p>

            <p className="warehouse-info-value">
                {value || "-"}
            </p>
        </div>
    );
}