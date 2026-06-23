import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import {
    createPackage,
    getPackagesByTransfer,
} from "../../api/packages/packages.js";

export default function TransferPackages() {

    const { id } = useParams();

    const [quantity, setQuantity] = useState(1);
    const [packages, setPackages] = useState([]);
    const [loading, setLoading] = useState(false);

    const loadPackages = () => {
        getPackagesByTransfer(id)
            .then((res) => {
                setPackages(res.data || []);
            })
            .catch((err) => {
                console.log(err);
            });
    };

    useEffect(() => {
        loadPackages();
    }, [id]);

    const handleCreate = async () => {
        try {
            setLoading(true);

            await createPackage({
                transferId: Number(id),
                quantity: Number(quantity),
            });

            loadPackages();

            alert("Packages created successfully");
        } catch (error) {
            console.log(error);

            alert(
                error.response?.data?.message ||
                "CREATE_PACKAGE_ERROR"
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="transfer-package-page">

            <div className="package-create-card">

                <h2>
                    Package Management - Transfer #{id}
                </h2>

                <div className="package-form">

                    <label>
                        Number of Packages
                    </label>

                    <input
                        type="number"
                        min="1"
                        value={quantity}
                        onChange={(e) =>
                            setQuantity(e.target.value)
                        }
                    />

                    <button
                        onClick={handleCreate}
                        disabled={loading}
                    >
                        {loading
                            ? "Creating..."
                            : "Create Packages"}
                    </button>

                </div>

            </div>

            <div className="package-list-card">

                <h2>
                    Package List
                </h2>

                <table className="package-table">

                    <thead>
                    <tr>
                        <th>Package Code</th>
                        <th>Status</th>
                        <th>Print Label</th>
                    </tr>
                    </thead>

                    <tbody>

                    {packages.length === 0 && (
                        <tr>
                            <td
                                colSpan="3"
                                style={{
                                    textAlign: "center",
                                }}
                            >
                                No packages found
                            </td>
                        </tr>
                    )}

                    {packages.map((box) => (

                        <tr key={box.id}>

                            <td>
                                {box.packageCode}
                            </td>

                            <td>
                                {box.status}
                            </td>

                            <td>

                                <button
                                    onClick={() =>
                                        window.open(
                                            `/warehouse/packages/${box.id}/barcode`,
                                            "_blank"
                                        )
                                    }
                                >
                                    Print Label
                                </button>

                            </td>

                        </tr>

                    ))}

                    </tbody>

                </table>

            </div>

        </div>
    );
}