import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
    getPackages,
    closePackage,
} from "../../api/packages/packages";

import PackageTable from "../../main-components/warehouse/PackageTable";
import Loading from "../../components/warehouse/Loading";

export default function PackageList() {
    const navigate = useNavigate();

    const [packages, setPackages] = useState([]);
    const [loading, setLoading] = useState(true);

    const loadPackages = () => {
        setLoading(true);

        getPackages()
            .then((res) => {
                setPackages(res.data || []);
            })
            .catch((error) => {
                console.log(error);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    useEffect(() => {
        loadPackages();
    }, []);

    const handleView = (row) => {
        navigate(`/warehouse/packages/${row.id}`);
    };

    const handleClose = async (row) => {
        try {
            await closePackage(row.id);
            loadPackages();

            window.open(
                `/warehouse/packages/${row.id}/barcode`,
                "_blank"
            );
        } catch (error) {
            console.log(error);

            alert(
                error.response?.data?.message ||
                "PACKAGE_SEAL_ERROR"
            );
        }
    };

    if (loading) return <Loading />;

    return (
        <div className="p-6 bg-orange-50 min-h-screen">
            <h1 className="warehouse-page-title">
                Package Management
            </h1>

            <PackageTable
                packages={packages}
                onView={handleView}
                onClose={handleClose}
                onPrint={(row) =>
                    window.open(
                        `/warehouse/packages/${row.id}/barcode`,
                        "_blank"
                    )
                }
            />
        </div>
    );
}