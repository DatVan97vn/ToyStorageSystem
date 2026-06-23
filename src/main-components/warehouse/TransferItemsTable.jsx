import Table from "../../components/warehouse/Table";

export default function TransferItemsTable({
                                               items = [],
                                           }) {
    const columns = [
        {
            key: "barcode",
            title: "Barcode",
            render: (row) =>
                row.barcode ||
                row.product?.barcode ||
                "-",
        },
        {
            key: "productName",
            title: "Product",
            render: (row) =>
                row.productName ||
                row.product?.name ||
                row.product?.productName ||
                "-",
        },
        {
            key: "requestedQuantity",
            title: "Requested Qty",
            render: (row) =>
                row.requestedQuantity ?? 0,
        },
        {
            key: "pickedQuantity",
            title: "Picked Qty",
            render: (row) =>
                row.pickedQuantity ?? 0,
        },
        {
            key: "receivedQuantity",
            title: "Received Qty",
            render: (row) =>
                row.receivedQuantity ?? 0,
        },
    ];

    return (
        <Table
            columns={columns}
            data={items}
            emptyText="No Products Found"
        />
    );
}