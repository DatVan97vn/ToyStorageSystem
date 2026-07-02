import { useEffect, useState } from "react";
import {
    getShippingManifests,
    updateManifestStatus,
} from "../../api/manifests/manifests";

import ManifestTable from "../../main-components/warehouse/ManifestTable";
import Loading from "../../components/warehouse/Loading";
import "../../assets/css/warehouse/ManifestList.css";

export default function ManifestList() {
    const [manifests, setManifests] = useState([]);
    const [selectedManifest, setSelectedManifest] = useState(null);
    const [loading, setLoading] = useState(true);

    const loadManifests = () => {
        setLoading(true);

        getShippingManifests()
            .then((res) => {
                setManifests(res.data || []);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    useEffect(() => {
        loadManifests();
    }, []);

    const handleConfirm = async (row) => {
        await updateManifestStatus(row.id, "READY");
        loadManifests();
    };

    if (loading) return <Loading />;

    return (
        <div className="manifest-page">
            <div className="manifest-header">
                <div>
                    <h1 className="manifest-title">
                        Shipping Manifest
                    </h1>

                    <p className="manifest-subtitle">
                        Manage manifests, packages, and shipping status
                    </p>
                </div>
            </div>

            <ManifestTable
                manifests={manifests}
                onView={(row) => setSelectedManifest(row)}
                onConfirm={handleConfirm}
            />

            {selectedManifest && (
                <div className="manifest-detail-card">
                    <div className="manifest-detail-header">
                        <h2 className="manifest-detail-title">
                            Manifest Details
                        </h2>

                        <button
                            className="manifest-close-btn"
                            onClick={() => setSelectedManifest(null)}
                        >
                            Close
                        </button>
                    </div>

                    <div className="manifest-grid">
                        <Info
                            label="Manifest Code"
                            value={selectedManifest.manifestCode}
                        />

                        <Info
                            label="Status"
                            value={selectedManifest.status}
                        />

                        <Info
                            label="From Warehouse"
                            value={selectedManifest.fromWarehouseName}
                        />

                        <Info
                            label="To Warehouse"
                            value={selectedManifest.toWarehouseName}
                        />

                        <Info
                            label="Created By"
                            value={selectedManifest.createdByUsername}
                        />

                        <Info
                            label="Created Date"
                            value={
                                selectedManifest.createdAt
                                    ? new Date(
                                        selectedManifest.createdAt
                                    ).toLocaleString("en-US")
                                    : "-"
                            }
                        />
                    </div>
                </div>
            )}
        </div>
    );
}

function Info({ label, value }) {
    return (
        <div className="manifest-info-box">
            <p className="manifest-info-label">
                {label}
            </p>

            <p className="manifest-info-value">
                {value || "-"}
            </p>
        </div>
    );
}