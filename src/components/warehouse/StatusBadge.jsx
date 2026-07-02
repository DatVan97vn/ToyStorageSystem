export default function StatusBadge({ status }) {
    const statusMap = {
        DRAFT: "bg-gray-100 text-gray-700",
        CHECKING: "bg-yellow-100 text-yellow-700",
        APPROVED: "bg-blue-100 text-blue-700",
        COMPLETED: "bg-green-100 text-green-700",
        REJECTED: "bg-red-100 text-red-700",
        CANCELLED: "bg-red-100 text-red-700",
    };

    return (
        <span
            className={`
                inline-flex px-3 py-1 rounded-full text-sm font-medium
                ${statusMap[status] || "bg-orange-100 text-orange-700"}
            `}
        >
            {status || "UNKNOWN"}
        </span>
    );
}