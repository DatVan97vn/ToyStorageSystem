import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import QRCode from "qrcode";
import ExcelJS from "exceljs";
import { saveAs } from "file-saver";

import {
  getGoodsReceiptById,
  getGoodsReceiptItems,
} from "../api/goodsReceiptApi";

function GoodsReceiptDetail() {
  const { id } = useParams();

  const [receipt, setReceipt] = useState(null);
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadReceipt = async () => {
    try {
      const [receiptRes, itemsRes] = await Promise.all([
        getGoodsReceiptById(id),
        getGoodsReceiptItems(id),
      ]);

      setReceipt(receiptRes.data);
      setItems(Array.isArray(itemsRes.data) ? itemsRes.data : []);
    } catch (error) {
      console.error("Lỗi tải chi tiết phiếu nhập:", error);
      alert("Không tải được chi tiết phiếu nhập");
    } finally {
      setLoading(false);
    }
  };

  const handleExportExcel = async () => {
    if (!receipt) {
      alert("Chưa có dữ liệu phiếu nhập");
      return;
    }

    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet("Phieu kiem hang");

    worksheet.mergeCells("A1:H1");
    worksheet.getCell("A1").value = "PHIẾU KIỂM HÀNG NHẬP KHO";
    worksheet.getCell("A1").font = { bold: true, size: 16 };
    worksheet.getCell("A1").alignment = { horizontal: "center" };

    worksheet.getCell("A3").value = "Mã phiếu";
    worksheet.getCell("B3").value = receipt.receiptCode;

    worksheet.getCell("A4").value = "Kho nhận";
    worksheet.getCell("B4").value = receipt.warehouseName;

    worksheet.getCell("A5").value = "Trạng thái";
    worksheet.getCell("B5").value = receipt.status;

    worksheet.getCell("A6").value = "Ngày tạo";
    worksheet.getCell("B6").value = receipt.createdAt;

    worksheet.getCell("A7").value = "Bắt đầu nhận";
    worksheet.getCell("B7").value = receipt.startedReceiveAt || "Chưa bắt đầu";

    worksheet.getCell("A8").value = "Hoàn tất nhận";
    worksheet.getCell("B8").value = receipt.completedReceiveAt || "Chưa hoàn tất";

    worksheet.getCell("G3").value = "QR mã phiếu";
    worksheet.getCell("G3").font = { bold: true };

    const qrContent = JSON.stringify({
      type: "GOODS_RECEIPT",
      id: receipt.id,
      code: receipt.receiptCode,
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
      "SKU",
      "Barcode",
      "Tên sản phẩm",
      "SL dự kiến",
      "SL đã nhận",
      "Chênh lệch",
      "Ghi chú",
    ];
    headerRow.font = { bold: true };

    items.forEach((item, index) => {
      worksheet.addRow([
        index + 1,
        item.productSku,
        item.productBarcode,
        item.productName,
        item.expectedQuantity,
        item.receivedQuantity,
        Number(item.expectedQuantity) - Number(item.receivedQuantity),
        "",
      ]);
    });

    const signatureStartRow = 13 + items.length;

    worksheet.getCell(`A${signatureStartRow}`).value = "Người giao";
    worksheet.getCell(`D${signatureStartRow}`).value = "Người nhận";
    worksheet.getCell(`G${signatureStartRow}`).value = "Thủ kho";

    worksheet.getCell(`A${signatureStartRow + 3}`).value = "Ký tên";
    worksheet.getCell(`D${signatureStartRow + 3}`).value = "Ký tên";
    worksheet.getCell(`G${signatureStartRow + 3}`).value = "Ký tên";

    worksheet.columns = [
      { width: 8 },
      { width: 16 },
      { width: 18 },
      { width: 28 },
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

    saveAs(blob, `${receipt.receiptCode}-kiem-hang-qr.xlsx`);
  };

  useEffect(() => {
    loadReceipt();
  }, [id]);

  if (loading) {
    return <p>Đang tải dữ liệu...</p>;
  }

  if (!receipt) {
    return <p>Không tìm thấy phiếu nhập</p>;
  }

  return (
    <div>
      <h1>Chi tiết phiếu nhập hàng</h1>

      <button
        className="primary-btn"
        type="button"
        onClick={handleExportExcel}
      >
        Xuất Excel kiểm hàng
      </button>

      <br />
      <br />

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
        <tbody>
          <tr>
            <th>ID</th>
            <td>{receipt.id}</td>
          </tr>

          <tr>
            <th>Mã phiếu</th>
            <td>{receipt.receiptCode}</td>
          </tr>

          <tr>
            <th>Kho nhận</th>
            <td>{receipt.warehouseName}</td>
          </tr>

          <tr>
            <th>Trạng thái</th>
            <td>{receipt.status}</td>
          </tr>

          <tr>
            <th>Ngày tạo</th>
            <td>{receipt.createdAt}</td>
          </tr>

          <tr>
            <th>Bắt đầu nhận</th>
            <td>{receipt.startedReceiveAt || "Chưa bắt đầu"}</td>
          </tr>

          <tr>
            <th>Hoàn tất nhận</th>
            <td>{receipt.completedReceiveAt || "Chưa hoàn tất"}</td>
          </tr>
        </tbody>
      </table>

      <br />

      <h3>Danh sách sản phẩm</h3>

      <table border="1" cellPadding="10" cellSpacing="0" width="100%">
        <thead>
          <tr>
            <th>STT</th>
            <th>SKU</th>
            <th>Barcode</th>
            <th>Tên sản phẩm</th>
            <th>SL dự kiến</th>
            <th>SL đã nhận</th>
          </tr>
        </thead>

        <tbody>
          {items.length === 0 ? (
            <tr>
              <td colSpan="6">Chưa có sản phẩm</td>
            </tr>
          ) : (
            items.map((item, index) => (
              <tr key={item.id}>
                <td>{index + 1}</td>
                <td>{item.productSku}</td>
                <td>{item.productBarcode}</td>
                <td>{item.productName}</td>
                <td>{item.expectedQuantity}</td>
                <td>{item.receivedQuantity}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default GoodsReceiptDetail;