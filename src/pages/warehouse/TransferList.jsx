import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getTransfers } from "../../api/transfers/transfers";
import TransferTable from "../../main-components/warehouse/TransferTable";
import Loading from "../../components/warehouse/Loading";

export default function TransferList() {
    const navigate = useNavigate();

    const [transfers, setTransfers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getTransfers()
            .then((res) => setTransfers(res.data || []))
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <Loading />;

    return (
        <div className="p-6 bg-orange-50 min-h-screen">
            <h1 className="warehouse-page-title">
                Transfer Orders
            </h1>

            <TransferTable
                transfers={transfers}
                onView={(row) =>
                    navigate(`/warehouse/transfers/${row.id}`)
                }
                onCheck={(row) =>
                    navigate(`/warehouse/transfers/${row.id}/check`)
                }
            />
        </div>
    );
}