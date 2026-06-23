import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getTransferById } from "../../api/transfers/transfers";

import TransferInfoCard from "../../main-components/warehouse/TransferInfoCard";
import TransferItemsTable from "../../main-components/warehouse/TransferItemsTable";
import Loading from "../../components/warehouse/Loading";

import "../../assets/css/warehouse/TransferDetail.css";

export default function TransferDetail() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [transfer, setTransfer] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getTransferById(id)
            .then((res) => {
                console.log("TRANSFER DETAIL DATA:", res.data);
                setTransfer(res.data);
            })
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) return <Loading />;

    if (!transfer) {
        return <p>Transfer Order Not Found</p>;
    }

    return (
        <div className="transfer-detail-page">
            <div className="transfer-detail-header">
                <div>
                    <h1 className="transfer-detail-title">
                        Transfer Order Details
                    </h1>

                    <p className="transfer-detail-subtitle">
                        View transfer information, source warehouse,
                        destination warehouse, and product list
                    </p>
                </div>

                <button
                    className="btn-orange-large"
                    onClick={() =>
                        navigate(`/warehouse/transfers/${id}/check`)
                    }
                >
                    Start Checking
                </button>
            </div>

            <TransferInfoCard transfer={transfer} />

            <div className="transfer-detail-card">
                <h2 className="transfer-detail-section-title">
                    Products in Transfer Order
                </h2>

                <TransferItemsTable
                    items={transfer.items || []}
                />
            </div>
        </div>
    );
}