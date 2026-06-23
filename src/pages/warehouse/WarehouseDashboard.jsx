import { useEffect, useState } from "react";
import { getTransfers } from "../../api/transfers/transfers.js";

export default function WarehouseDashboard() {
    const [transfers, setTransfers] = useState([]);

    useEffect(() => {
        getTransfers().then((res) => {
            setTransfers(res.data || []);
        });
    }, []);

    const draft =
        transfers.filter((x) => x.status === "DRAFT").length;

    const checking =
        transfers.filter((x) => x.status === "CHECKING").length;

    const completed =
        transfers.filter((x) => x.status === "COMPLETED").length;

    return (
        <div className="p-6 bg-orange-50 min-h-screen">
            <h1 className="text-2xl font-bold text-orange-600 mb-6">
                Warehouse Dashboard
            </h1>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">

                <StatCard
                    title="Pending Transfers"
                    value={draft}
                />

                <StatCard
                    title="Transfers in Progress"
                    value={checking}
                />

                <StatCard
                    title="Completed Transfers"
                    value={completed}
                />

            </div>
        </div>
    );
}

function StatCard({ title, value }) {
    return (
        <div className="bg-white rounded-xl shadow p-5 border-l-4 border-orange-500">
            <p className="text-gray-500">
                {title}
            </p>

            <h2 className="text-3xl font-bold text-orange-600">
                {value}
            </h2>
        </div>
    );
}