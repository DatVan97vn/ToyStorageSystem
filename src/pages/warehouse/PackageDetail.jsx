import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { getPackageById } from "../../api/packages/packages";
import StatusBadge from "../../components/warehouse/StatusBadge";

import "../../assets/css/warehouse/PackageDetail.css";

export default function PackageDetail() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [packageBox, setPackageBox] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getPackageById(id)
            .then((res) => {
                setPackageBox(res.data);
            })
            .catch((error) => {
                console.log(error);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);

    if (loading) {
        return (
            <div className="package-detail-loading">
                Loading package details...
            </div>
        );
    }

    if (!packageBox) {
        return (
            <div className="package-detail-loading">
                Package not found
            </div>
        );
    }

    return (
        <div className="package-detail-page">
            <div className="package-detail-header">
                <div>
                    <h1 className="package-detail-title">
                        Package Details
                    </h1>

                    <p className="package-detail-subtitle">
                        View package information, transfer order, and warehouse route
                    </p>
                </div>

                <div className="package-detail-actions">
                    <button
                        className="package-detail-btn package-detail-btn-outline"
                        onClick={() => navigate(-1)}
                    >
                        Back
                    </button>

                    <button
                        className="package-detail-btn package-detail-btn-filled"
                        onClick={() =>
                            window.open(
                                `/warehouse/packages/${packageBox.id}/barcode`,
                                "_blank"
                            )
                        }
                    >
                        Print Label
                    </button>
                </div>
            </div>

            <div className="package-detail-card">
                <div className="package-detail-card-header">
                    <div>
                        <p className="package-detail-label">
                            Package Code
                        </p>

                        <h2 className="package-detail-code">
                            {packageBox.packageCode || "-"}
                        </h2>
                    </div>

                    <StatusBadge status={packageBox.status} />
                </div>

                <div className="package-detail-grid">
                    <InfoItem
                        label="Transfer Order"
                        value={packageBox.transferCode}
                    />

                    <InfoItem
                        label="Source Warehouse"
                        value={packageBox.fromWarehouseName}
                    />

                    <InfoItem
                        label="Destination Warehouse"
                        value={packageBox.toWarehouseName}
                    />

                    <InfoItem
                        label="Package Status"
                        value={packageBox.status}
                    />

                    <InfoItem
                        label="Warehouse"
                        value={packageBox.warehouseName}
                    />

                    <InfoItem
                        label="Note"
                        value={packageBox.note}
                    />
                </div>
            </div>
        </div>
    );
}

function InfoItem({ label, value }) {
    return (
        <div className="package-detail-info-item">
            <p className="package-detail-info-label">
                {label}
            </p>

            <p className="package-detail-info-value">
                {value || "-"}
            </p>
        </div>
    );
}