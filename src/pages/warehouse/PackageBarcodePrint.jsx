import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import JsBarcode from "jsbarcode";

import { getPackageById } from "../../api/packages/packages";

import "../../assets/css/warehouse/PackageBarcodePrint.css";

export default function PackageBarcodePrint() {
    const { id } = useParams();

    const [packageBox, setPackageBox] = useState(null);
    const [loading, setLoading] = useState(true);

    const barcodeRef = useRef(null);

    useEffect(() => {
        getPackageById(id)
            .then((res) => {
                setPackageBox(res.data);
            })
            .catch((error) => {
                console.log(error);

                alert(
                    error.response?.data?.message ||
                    "PACKAGE_NOT_FOUND"
                );
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id]);

    useEffect(() => {
        if (!packageBox || !barcodeRef.current) return;

        JsBarcode(barcodeRef.current, packageBox.packageCode || "-", {
            format: "CODE128",
            width: 2,
            height: 80,
            displayValue: true,
            fontSize: 18,
            margin: 10,
        });
    }, [packageBox]);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!packageBox) {
        return <div>Package not found</div>;
    }

    return (
        <div className="package-print-page">
            <div className="package-print-a6">
                <h1 className="package-print-title">
                    PACKAGE LABEL
                </h1>

                <div className="barcode-wrapper">
                    <svg ref={barcodeRef}></svg>
                </div>

                <div className="package-print-info">
                    <div className="info-row">
                        <span>Package Code:</span>
                        <strong>{packageBox.packageCode || "-"}</strong>
                    </div>

                    <div className="info-row">
                        <span>Transfer Order:</span>
                        <strong>{packageBox.transferCode || "-"}</strong>
                    </div>

                    <div className="info-row">
                        <span>Source Warehouse:</span>
                        <strong>{packageBox.fromWarehouseName || "-"}</strong>
                    </div>

                    <div className="info-row">
                        <span>Destination Warehouse:</span>
                        <strong>{packageBox.toWarehouseName || "-"}</strong>
                    </div>
                </div>

                <button
                    className="print-btn no-print"
                    onClick={() => window.print()}
                >
                    Print Label
                </button>
            </div>
        </div>
    );
}