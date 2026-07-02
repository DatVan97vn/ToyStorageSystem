import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import {
    getTransferById,
    scanTransferBarcode,
    updateTransferStatus,
} from "../../api/transfers/transfers";

import BarcodeScannerBox from "../../main-components/warehouse/BarcodeScannerBox";
import TransferItemsTable from "../../main-components/warehouse/TransferItemsTable";
import Loading from "../../components/warehouse/Loading";

import "../../assets/css/warehouse/TransferCheck.css";

export default function TransferCheck() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [transfer, setTransfer] = useState(null);
    const [loading, setLoading] = useState(true);
    const [scanLoading, setScanLoading] = useState(false);
    const [showScanner, setShowScanner] = useState(false);

    const loadTransfer = () => {
        getTransferById(id)
            .then((res) => setTransfer(res.data))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        loadTransfer();
    }, [id]);

    const handleStatus = async (status) => {
        await updateTransferStatus(id, status);
        loadTransfer();
    };

    const handleScan = async (data) => {
        setScanLoading(true);

        try {
            await scanTransferBarcode(id, {
                ...data,
                transferId: Number(id),
                scanType: "PICK",
            });

            loadTransfer();
        } catch (error) {
            alert(
                error.response?.data?.message ||
                error.response?.data?.error ||
                "SCAN_ERROR"
            );
        } finally {
            setScanLoading(false);
        }
    };

    const isPickedEnough =
        (transfer?.items || []).length > 0 &&
        (transfer?.items || []).every(
            (item) =>
                Number(item.pickedQuantity || 0) >=
                Number(item.requestedQuantity || 0)
        );

    if (loading) return <Loading />;

    return (
        <div className="transfer-check-page">
            <div className="transfer-check-header">
                <div>
                    <h1 className="transfer-check-title">
                        Transfer Check {transfer?.transferCode}
                    </h1>

                    <p className="transfer-check-subtitle">
                        Current Status: {transfer?.status}
                    </p>
                </div>

                <div className="transfer-check-actions">
                    {transfer?.status === "APPROVED" && (
                        <button
                            className="btn-orange"
                            onClick={() => handleStatus("PICKING")}
                        >
                            Start Picking
                        </button>
                    )}

                    {transfer?.status === "PICKING" && (
                        <>
                            <button
                                className="btn-orange"
                                onClick={() => setShowScanner(!showScanner)}
                            >
                                {showScanner ? "Hide Barcode Scanner" : "Open Barcode Scanner"}
                            </button>

                            <button
                                className="btn-outline-orange"
                                onClick={() => handleStatus("PACKING")}
                                disabled={!isPickedEnough}
                            >
                                Move to Packing
                            </button>
                        </>
                    )}

                    {transfer?.status === "PACKING" && (
                        <>
                            <button
                                className="btn-green"
                                onClick={() =>
                                    navigate(`/warehouse/transfers/${id}/packages`)
                                }
                            >
                                Manage Packages
                            </button>

                            <button
                                className="btn-dark"
                                onClick={() => handleStatus("SHIPPED")}
                            >
                                Ship Out
                            </button>
                        </>
                    )}

                    {transfer?.status === "SHIPPED" && (
                        <button
                            className="btn-orange"
                            onClick={() => handleStatus("RECEIVING")}
                        >
                            Start Receiving
                        </button>
                    )}

                    {transfer?.status === "RECEIVING" && (
                        <button
                            className="btn-dark"
                            onClick={() => handleStatus("COMPLETED")}
                        >
                            Complete Transfer
                        </button>
                    )}
                </div>
            </div>

            {showScanner && (
                <div className="transfer-check-card">
                    <BarcodeScannerBox
                        onScan={handleScan}
                        loading={scanLoading}
                    />
                </div>
            )}

            <div className="transfer-check-card">
                <div className="transfer-check-card-header">
                    <h2>Product List</h2>

                    <span>
                        {isPickedEnough ? "Picking Completed" : "Picking Incomplete"}
                    </span>
                </div>

                <TransferItemsTable items={transfer?.items || []} />
            </div>
        </div>
    );
}