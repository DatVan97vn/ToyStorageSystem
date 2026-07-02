import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import {
    getShippingManifestById,
} from "../../api/manifests/manifests";

import "../../assets/css/warehouse/ManifestDetail.css";

export default function ManifestDetail() {

    const { id } = useParams();

    const [manifest, setManifest] = useState(null);

    useEffect(() => {

        getShippingManifestById(id)
            .then((res) => {
                setManifest(res.data);
            });

    }, [id]);

    if (!manifest) {
        return (
            <div className="loading-container">
                Loading data...
            </div>
        );
    }

    return (
        <div className="page-manifest-detail">

            <div className="manifest-header">
                <h1 className="manifest-title">
                    Manifest Details
                </h1>
            </div>

            <div className="manifest-card">

                <div className="manifest-info-grid">

                    <div className="manifest-info-item">
                        <span className="manifest-label">
                            Manifest Code
                        </span>

                        <span className="manifest-value">
                            {manifest.manifestCode}
                        </span>
                    </div>

                    <div className="manifest-info-item">
                        <span className="manifest-label">
                            Status
                        </span>

                        <span className="status-badge">
                            {manifest.status}
                        </span>
                    </div>

                    <div className="manifest-info-item">
                        <span className="manifest-label">
                            From Warehouse
                        </span>

                        <span className="manifest-value">
                            {manifest.fromWarehouseName}
                        </span>
                    </div>

                    <div className="manifest-info-item">
                        <span className="manifest-label">
                            To Warehouse
                        </span>

                        <span className="manifest-value">
                            {manifest.toWarehouseName}
                        </span>
                    </div>

                    <div className="manifest-info-item">
                        <span className="manifest-label">
                            Created By
                        </span>

                        <span className="manifest-value">
                            {manifest.createdByUsername}
                        </span>
                    </div>

                    <div className="manifest-info-item">
                        <span className="manifest-label">
                            Created Date
                        </span>

                        <span className="manifest-value">
                            {manifest.createdAt}
                        </span>
                    </div>

                </div>

            </div>

            <div className="manifest-section">

                <h2 className="manifest-section-title">
                    Package List
                </h2>

                <div className="manifest-card">
                    No packages available.
                </div>

            </div>

        </div>
    );
}