import "../../assets/css/warehouse/WarehouseTable.css";

export default function Table({
                                  columns = [],
                                  data = [],
                                  emptyText = "Không có dữ liệu",
                              }) {
    return (
        <div className="warehouse-table-card">
            <table className="warehouse-table">
                <thead>
                <tr>
                    {columns.map((col) => (
                        <th key={col.key}>
                            {col.title}
                        </th>
                    ))}
                </tr>
                </thead>

                <tbody>
                {data.length === 0 && (
                    <tr>
                        <td
                            colSpan={columns.length}
                            className="warehouse-table-empty"
                        >
                            {emptyText}
                        </td>
                    </tr>
                )}

                {data.map((row, index) => (
                    <tr key={row.id || index}>
                        {columns.map((col) => (
                            <td key={col.key}>
                                {col.render
                                    ? col.render(row, index)
                                    : row[col.key]}
                            </td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}