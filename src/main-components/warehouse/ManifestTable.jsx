import { useNavigate } from "react-router-dom";
import Button from "../../components/warehouse/Button";
import StatusBadge from "../../components/warehouse/StatusBadge";
import "../../assets/css/warehouse/ManifestTable.css";

export default function ManifestTable({
                                          manifests = [],
                                          onConfirm,
                                      }) {
    const navigate = useNavigate();

    return (
        <div className="manifest-table-card">
            <table className="manifest-table">
                <thead>
                <tr>
                    <th>Manifest Code</th>
                    <th>Package Count</th>
                    <th>Driver</th>
                    <th>Vehicle Number</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>

                <tbody>
                {manifests.length === 0 && (
                    <tr>
                        <td
                            colSpan="6"
                            className="manifest-empty"
                        >
                            No Shipping Manifests Found
                        </td>
                    </tr>
                )}

                {manifests.map((row) => (
                    <tr key={row.id}>
                        <td className="manifest-code">
                            {row.manifestCode || "-"}
                        </td>

                        <td>
                            {row.packageCount ?? 0}
                        </td>

                        <td>
                            {row.driverName ||
                                row.driver?.name ||
                                "-"}
                        </td>

                        <td>
                            {row.vehicleNumber ||
                                row.vehicleNo ||
                                "-"}
                        </td>

                        <td>
                            <StatusBadge
                                status={row.status}
                            />
                        </td>

                        <td>
                            <div className="manifest-action-group">

                                <Button
                                    className="manifest-btn manifest-btn-outline"
                                    onClick={() =>
                                        navigate(
                                            `/warehouse/manifests/${row.id}`
                                        )
                                    }
                                >
                                    View Details
                                </Button>

                                {row.status === "DRAFT" && (
                                    <Button
                                        className="manifest-btn manifest-btn-filled"
                                        onClick={() =>
                                            onConfirm?.(row)
                                        }
                                    >
                                        Confirm
                                    </Button>
                                )}

                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}