import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  approveTransfer,
  completeTransfer,
  getTransferById,
} from "../api/transferApi";
import QRCode from "qrcode";
import ExcelJS from "exceljs";
import { saveAs } from "file-saver";

function TransferDetail() {
  const { id } = useParams();
  const [transfer, setTransfer] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadTransfer = async () => {
    try {
      const res = await getTransferById(id);
      setTransfer(res.data);
    } catch (error) {
      console.error("Lỗi lấy chi tiết phiếu:", error);
      alert("Không lấy được chi tiết phiếu");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTransfer();
  }, [id]);

  const handleApprove = async () => {
    try {
      await approveTransfer(id);
      alert("Duyệt phiếu thành công");
      loadTransfer();
    } catch (error) {
      console.error("Lỗi duyệt phiếu:", error);
      alert("Duyệt phiếu thất bại");
    }
  };

  const handleComplete = async () => {
    try {
      await completeTransfer(id);
      alert("Hoàn tất phiếu thành công");
      loadTransfer();
    } catch (error) {
      console.error("Lỗi hoàn tất phiếu:", error);
      alert("Hoàn tất phiếu thất bại");
    }
  };
  const handleExportExcel = async () => {
  if (!transfer) {
    alert("Chưa có dữ liệu phiếu điều kho");
    return;
  }

  const fromWarehouseName =
    transfer.fromWarehouse?.warehouseName ||
    transfer.fromWarehouse?.name ||
    "Chưa cập nhật";

  const toWarehouseName =
    transfer.toWarehouse?.warehouseName ||
    transfer.toWarehouse?.name ||
    "Chưa cập nhật";

  const workbook = new ExcelJS.Workbook();
  const worksheet = workbook.addWorksheet("Phieu dieu kho");

  worksheet.mergeCells("A1:H1");
  worksheet.getCell("A1").value = "PHIẾU ĐIỀU KHO";
  worksheet.getCell("A1").font = { bold: true, size: 16 };
  worksheet.getCell("A1").alignment = { horizontal: "center" };

  worksheet.getCell("A3").value = "Mã phiếu";
  worksheet.getCell("B3").value = transfer.transferCode;

  worksheet.getCell("A4").value = "Trạng thái";
  worksheet.getCell("B4").value = transfer.status;

  worksheet.getCell("A5").value = "Kho xuất";
  worksheet.getCell("B5").value = fromWarehouseName;

  worksheet.getCell("A6").value = "Kho nhập";
  worksheet.getCell("B6").value = toWarehouseName;

  worksheet.getCell("A7").value = "Ngày tạo";
  worksheet.getCell("B7").value = transfer.createdAt;

  worksheet.getCell("A8").value = "Ngày hoàn tất";
  worksheet.getCell("B8").value = transfer.completedAt || "Chưa hoàn tất";

  worksheet.getCell("G3").value = "QR mã phiếu";
  worksheet.getCell("G3").font = { bold: true };

  const qrContent = JSON.stringify({
    type: "TRANSFER",
    id: transfer.id,
    code: transfer.transferCode,
  });

  const qrDataUrl = await QRCode.toDataURL(qrContent);
  const qrBase64 = qrDataUrl.replace(/^data:image\/png;base64,/, "");

  const qrImageId = workbook.addImage({
    base64: qrBase64,
    extension: "png",
  });

  worksheet.addImage(qrImageId, {
    tl: { col: 6, row: 3 },
    ext: { width: 120, height: 120 },
  });

  const headerRow = worksheet.getRow(11);
  headerRow.values = [
    "STT",
    "Mã sản phẩm",
    "Barcode",
    "Tên sản phẩm",
    "SL yêu cầu",
    "SL thực tế",
    "Chênh lệch",
    "Ghi chú",
  ];
  headerRow.font = { bold: true };

  const transferItems = Array.isArray(transfer.items) ? transfer.items : [];

  if (transferItems.length === 0) {
    worksheet.addRow([
      "",
      "",
      "",
      "Backend hiện chưa trả danh sách sản phẩm",
      "",
      "",
      "",
      "",
    ]);
  } else {
    transferItems.forEach((item, index) => {
      const requestedQty = Number(
        item.requestedQuantity ||
          item.quantity ||
          item.requestedQty ||
          0
      );
      const actualQty = Number(item.actualQty || item.pickedQuantity || 0);

      worksheet.addRow([
        index + 1,
        item.productCode || item.sku || item.productId || "",
        item.barcode || item.productBarcode || "",
        item.productName || "Chưa có tên sản phẩm",
        requestedQty,
        actualQty,
        requestedQty - actualQty,
        "",
      ]);
    });
  }

  const signatureStartRow = 13 + Math.max(transferItems.length, 1);

  worksheet.getCell(`A${signatureStartRow}`).value = "Người lập phiếu";
  worksheet.getCell(`D${signatureStartRow}`).value = "Người xuất kho";
  worksheet.getCell(`G${signatureStartRow}`).value = "Người nhận kho";

  worksheet.getCell(`A${signatureStartRow + 3}`).value = "Ký tên";
  worksheet.getCell(`D${signatureStartRow + 3}`).value = "Ký tên";
  worksheet.getCell(`G${signatureStartRow + 3}`).value = "Ký tên";

  worksheet.columns = [
    { width: 8 },
    { width: 18 },
    { width: 18 },
    { width: 30 },
    { width: 14 },
    { width: 14 },
    { width: 14 },
    { width: 20 },
  ];

  worksheet.eachRow((row) => {
    row.eachCell((cell) => {
      cell.alignment = { vertical: "middle" };
    });
  });

  const buffer = await workbook.xlsx.writeBuffer();

  const blob = new Blob([buffer], {
    type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
  });

  saveAs(blob, `${transfer.transferCode}-dieu-kho.xlsx`);
};
  if (loading) return <p>Đang tải chi tiết phiếu...</p>;

  if (!transfer) return <p>Không tìm thấy phiếu</p>;

  return (
    <div>
      <h1>Chi tiết phiếu điều kho</h1>
      <button className="primary-btn" type="button" onClick={handleExportExcel}>
        Xuất Excel
      </button>

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
        <tbody>
          <tr>
            <th>ID</th>
            <td>{transfer.id}</td>
          </tr>
          <tr>
            <th>Mã phiếu</th>
            <td>{transfer.transferCode}</td>
          </tr>
          <tr>
            <th>Trạng thái</th>
            <td>{transfer.status}</td>
          </tr>
          <tr>
            <th>Kho xuất</th>
            <td>
              {transfer.fromWarehouse
                ? transfer.fromWarehouse.name
                : "Backend chưa trả dữ liệu kho xuất"}
            </td>
          </tr>
          <tr>
            <th>Kho nhập</th>
            <td>
              {transfer.toWarehouse
                ? transfer.toWarehouse.name
                : "Backend chưa trả dữ liệu kho nhập"}
            </td>
          </tr>
          <tr>
            <th>Ngày tạo</th>
            <td>{transfer.createdAt}</td>
          </tr>
          <tr>
            <th>Ngày hoàn tất</th>
            <td>{transfer.completedAt || "Chưa hoàn tất"}</td>
          </tr>
        </tbody>
      </table>

      <br />

      <h3>Danh sách sản phẩm trong phiếu</h3>

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
        <thead>
          <tr>
            <th>STT</th>
            <th>Mã sản phẩm</th>
            <th>Tên sản phẩm</th>
            <th>Số lượng</th>
          </tr>
        </thead>

        <tbody>
          {!transfer.items || transfer.items.length === 0 ? (
            <tr>
              <td colSpan="4">Backend hiện chưa trả danh sách sản phẩm</td>
            </tr>
          ) : (
            transfer.items.map((item, index) => (
              <tr key={item.id || index}>
                <td>{index + 1}</td>
                <td>{item.productCode || item.sku || item.productId}</td>
                <td>{item.productName || "Chưa có tên sản phẩm"}</td>
                <td>
                  {item.requestedQuantity ||
                    item.quantity ||
                    item.requestedQty}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <br />

      <button onClick={handleApprove} disabled={transfer.status === "APPROVED"}>
        Duyệt phiếu
      </button>

      <button
        onClick={handleComplete}
        style={{ marginLeft: "12px" }}
        disabled={transfer.status === "COMPLETED"}
      >
        Hoàn tất phiếu
      </button>
    </div>
  );
}

export default TransferDetail;