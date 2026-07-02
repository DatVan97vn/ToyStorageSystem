import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import QRCode from "qrcode";
import ExcelJS from "exceljs";
import { saveAs } from "file-saver";

import {
  getGoodsReceiptById,
  getGoodsReceiptItems,
} from "../../api/business/goodsReceiptApi";

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
  const getReceiptItems = () => {
    return (
      items ||
      receipt?.items ||
      receipt?.goodsReceiptItems ||
      receipt?.receiptItems ||
      receipt?.details ||
      []
    );
  };
const handleExportExcel = async () => {
  if (!receipt) {
    alert("Chưa có dữ liệu phiếu nhập");
    return;
  }

  const receiptItems = getReceiptItems();

  const workbook = new ExcelJS.Workbook();
  const worksheet = workbook.addWorksheet("Phieu kiem hang");

  worksheet.properties.defaultRowHeight = 22;
  worksheet.pageSetup = {
    paperSize: 9,
    orientation: "landscape",
    fitToPage: true,
    fitToWidth: 1,
    fitToHeight: 0,
    margins: {
      left: 0.3,
      right: 0.3,
      top: 0.4,
      bottom: 0.4,
      header: 0.2,
      footer: 0.2,
    },
  };

  worksheet.columns = [
    { width: 8 },   // A - STT
    { width: 16 },  // B - SKU
    { width: 22 },  // C - Barcode
    { width: 34 },  // D - Tên sản phẩm
    { width: 14 },  // E - SL dự kiến
    { width: 14 },  // F - SL đã nhận
    { width: 14 },  // G - Chênh lệch
    { width: 22 },  // H - Ghi chú
  ];

  worksheet.mergeCells("A1:H1");
  worksheet.getRow(1).height = 34;
  worksheet.getCell("A1").value = "PHIẾU KIỂM HÀNG NHẬP KHO";
  worksheet.getCell("A1").font = { bold: true, size: 18 };
  worksheet.getCell("A1").alignment = {
    horizontal: "center",
    vertical: "middle",
  };

  worksheet.mergeCells("A2:H2");
  worksheet.getRow(2).height = 24;
  worksheet.getCell("A2").value = `Ngày lập: ${receipt.createdAt || ""}`;
  worksheet.getCell("A2").font = { bold: true, size: 12 };
  worksheet.getCell("A2").alignment = {
    horizontal: "center",
    vertical: "middle",
  };

  worksheet.getRow(3).height = 12;

  worksheet.mergeCells("A4:D4");
  worksheet.getCell("A4").value = "THÔNG TIN PHIẾU NHẬP";
  worksheet.getCell("A4").font = { bold: true, size: 13 };

  worksheet.getCell("A5").value = "Mã phiếu:";
  worksheet.getCell("B5").value = receipt.receiptCode || receipt.code || "";
  worksheet.mergeCells("B5:D5");

  worksheet.getCell("A6").value = "Kho nhận:";
  worksheet.getCell("B6").value =
    receipt.warehouseName || receipt.warehouse?.name || "Chưa cập nhật";
  worksheet.mergeCells("B6:D6");

  worksheet.getCell("A7").value = "Trạng thái:";
  worksheet.getCell("B7").value = receipt.status || "DRAFT";
  worksheet.mergeCells("B7:D7");

  worksheet.getCell("A8").value = "Bắt đầu nhận:";
  worksheet.getCell("B8").value =
    receipt.startedReceiveAt ||
    receipt.startedAt ||
    receipt.started_receive_at ||
    "Chưa bắt đầu";
  worksheet.mergeCells("B8:D8");

  worksheet.getCell("A9").value = "Hoàn tất nhận:";
  worksheet.getCell("B9").value =
    receipt.completedReceiveAt ||
    receipt.completedAt ||
    receipt.completed_receive_at ||
    "Chưa hoàn tất";
  worksheet.mergeCells("B9:D9");

  worksheet.getCell("F4").value = "QR mã phiếu";
  worksheet.getCell("F4").font = { bold: true, size: 12 };
  worksheet.getCell("F4").alignment = {
    horizontal: "center",
    vertical: "middle",
  };

  const qrContent = JSON.stringify({
    type: "GOODS_RECEIPT",
    id: receipt.id,
    code: receipt.receiptCode || receipt.code,
  });

  const qrDataUrl = await QRCode.toDataURL(qrContent);
  const qrBase64 = qrDataUrl.replace(/^data:image\/png;base64,/, "");

  const qrImageId = workbook.addImage({
    base64: qrBase64,
    extension: "png",
  });

  worksheet.addImage(qrImageId, {
    tl: { col: 5.2, row: 4.2 },
    ext: { width: 120, height: 120 },
  });

  for (let rowIndex = 5; rowIndex <= 9; rowIndex++) {
    worksheet.getRow(rowIndex).height = 24;
  }

  ["A5", "A6", "A7", "A8", "A9"].forEach((cellAddress) => {
    worksheet.getCell(cellAddress).font = { bold: true };
    worksheet.getCell(cellAddress).alignment = {
      horizontal: "left",
      vertical: "middle",
      wrapText: false,
      shrinkToFit: true,
    };
  });

  const headerRow = worksheet.getRow(12);
  headerRow.height = 26;
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
  headerRow.fill = {
    type: "pattern",
    pattern: "solid",
    fgColor: { argb: "FFFCEFE3" },
  };

  headerRow.eachCell((cell) => {
    cell.alignment = {
      horizontal: "center",
      vertical: "middle",
      wrapText: false,
    };
  });

  if (receiptItems.length === 0) {
    worksheet.addRow([
      "",
      "",
      "",
      "Chưa có sản phẩm trong phiếu",
      "",
      "",
      "",
      "",
    ]);
  } else {
    receiptItems.forEach((item, index) => {
      const expectedQty = Number(
        item.expectedQuantity ||
          item.expected_quantity ||
          item.quantity ||
          item.plannedQuantity ||
          0
      );

      const receivedQty = Number(
        item.receivedQuantity ||
          item.received_quantity ||
          item.actualQuantity ||
          item.checkedQuantity ||
          0
      );

      const difference = expectedQty - receivedQty;

      worksheet.addRow([
        index + 1,
        item.productSku || item.sku || item.productCode || "",
        item.productBarcode || item.barcode || item.product?.barcode || "",
        item.productName ||
          item.name ||
          item.product?.name ||
          "Chưa có tên sản phẩm",
        expectedQty,
        receivedQty,
        difference,
        "",
      ]);
    });
  }

  const firstItemRow = 13;
  const lastItemRow = 12 + Math.max(receiptItems.length, 1);

  for (let rowIndex = firstItemRow; rowIndex <= lastItemRow; rowIndex++) {
    worksheet.getRow(rowIndex).height = 24;

    worksheet.getCell(`A${rowIndex}`).alignment = {
      horizontal: "center",
      vertical: "middle",
    };

    worksheet.getCell(`B${rowIndex}`).alignment = {
      horizontal: "center",
      vertical: "middle",
    };

    worksheet.getCell(`C${rowIndex}`).alignment = {
      horizontal: "center",
      vertical: "middle",
    };

    worksheet.getCell(`D${rowIndex}`).alignment = {
      horizontal: "left",
      vertical: "middle",
      wrapText: true,
    };

    worksheet.getCell(`E${rowIndex}`).alignment = {
      horizontal: "right",
      vertical: "middle",
    };

    worksheet.getCell(`F${rowIndex}`).alignment = {
      horizontal: "right",
      vertical: "middle",
    };

    worksheet.getCell(`G${rowIndex}`).alignment = {
      horizontal: "right",
      vertical: "middle",
    };

    worksheet.getCell(`H${rowIndex}`).alignment = {
      horizontal: "left",
      vertical: "middle",
    };
  }

  for (let rowIndex = 12; rowIndex <= lastItemRow; rowIndex++) {
    worksheet.getRow(rowIndex).eachCell((cell) => {
      cell.border = {
        top: { style: "thin" },
        left: { style: "thin" },
        bottom: { style: "thin" },
        right: { style: "thin" },
      };
    });
  }

  const signatureStartRow = lastItemRow + 3;

  worksheet.mergeCells(`A${signatureStartRow}:B${signatureStartRow}`);
  worksheet.mergeCells(`D${signatureStartRow}:E${signatureStartRow}`);
  worksheet.mergeCells(`G${signatureStartRow}:H${signatureStartRow}`);

  worksheet.getCell(`A${signatureStartRow}`).value = "Người giao";
  worksheet.getCell(`D${signatureStartRow}`).value = "Người nhận";
  worksheet.getCell(`G${signatureStartRow}`).value = "Thủ kho";

  worksheet.mergeCells(`A${signatureStartRow + 3}:B${signatureStartRow + 3}`);
  worksheet.mergeCells(`D${signatureStartRow + 3}:E${signatureStartRow + 3}`);
  worksheet.mergeCells(`G${signatureStartRow + 3}:H${signatureStartRow + 3}`);

  worksheet.getCell(`A${signatureStartRow + 3}`).value = "Ký tên";
  worksheet.getCell(`D${signatureStartRow + 3}`).value = "Ký tên";
  worksheet.getCell(`G${signatureStartRow + 3}`).value = "Ký tên";

  [
    `A${signatureStartRow}`,
    `D${signatureStartRow}`,
    `G${signatureStartRow}`,
    `A${signatureStartRow + 3}`,
    `D${signatureStartRow + 3}`,
    `G${signatureStartRow + 3}`,
  ].forEach((cellAddress) => {
    worksheet.getCell(cellAddress).font = { bold: true };
    worksheet.getCell(cellAddress).alignment = {
      horizontal: "center",
      vertical: "middle",
      wrapText: false,
    };
  });

  for (let rowIndex = 1; rowIndex <= signatureStartRow + 3; rowIndex++) {
    worksheet.getRow(rowIndex).eachCell((cell) => {
      if (!cell.alignment || !cell.alignment.horizontal) {
        cell.alignment = {
          horizontal: "left",
          vertical: "middle",
          wrapText: false,
          shrinkToFit: true,
        };
      }
    });
  }

  const buffer = await workbook.xlsx.writeBuffer();

  const blob = new Blob([buffer], {
    type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
  });

  saveAs(blob, `${receipt.receiptCode || receipt.code}-kiem-hang-qr.xlsx`);
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
  <div className="page-container goods-detail-page">
    <div className="goods-detail-header">
      <div>
        <h1>Chi tiết phiếu nhập hàng</h1>
        <p>
          Xem thông tin phiếu nhập, danh sách sản phẩm và tiến độ kiểm nhận hàng.
        </p>
      </div>

      <button
        type="button"
        className="goods-excel-btn"
        onClick={handleExportExcel}
      >
        Xuất Excel kiểm hàng
      </button>
    </div>

    <div className="goods-detail-card">
      <div className="goods-card-title">
        <h2>Thông tin phiếu nhập</h2>

        <span
          className={`goods-status-badge ${
            receipt?.status === "COMPLETED"
              ? "completed"
              : receipt?.status === "APPROVED"
              ? "approved"
              : "draft"
          }`}
        >
          {receipt?.status || "DRAFT"}
        </span>
      </div>

      <div className="goods-info-grid">
        <div className="info-item">
          <span>ID</span>
          <strong>{receipt?.id}</strong>
        </div>

        <div className="info-item">
          <span>Mã phiếu</span>
          <strong>{receipt?.receiptCode || receipt?.code}</strong>
        </div>

        <div className="info-item">
          <span>Kho nhận</span>
          <strong>
            {receipt?.warehouseName ||
              receipt?.warehouse?.name ||
              "Chưa cập nhật"}
          </strong>
        </div>

        <div className="info-item">
          <span>Ngày tạo</span>
          <strong>{receipt?.createdAt}</strong>
        </div>

        <div className="info-item">
          <span>Bắt đầu nhận</span>
          <strong>{receipt?.startedAt || "Chưa bắt đầu"}</strong>
        </div>

        <div className="info-item">
          <span>Hoàn tất nhận</span>
          <strong>{receipt?.completedAt || "Chưa hoàn tất"}</strong>
        </div>
      </div>
    </div>

    <div className="goods-detail-card">
      <div className="goods-card-title">
        <h2>Danh sách sản phẩm</h2>
        <span>{getReceiptItems().length || 0} sản phẩm</span>
      </div>

      <div className="goods-table-scroll">
        <table className="goods-items-table">
          <thead>
            <tr>
              <th>STT</th>
              <th>SKU</th>
              <th>Barcode</th>
              <th>Tên sản phẩm</th>
              <th>SL dự kiến</th>
              <th>SL đã nhận</th>
              <th>Chênh lệch</th>
            </tr>
          </thead>

          <tbody>
            {getReceiptItems().length === 0 ? (
              <tr>
                <td colSpan="7" className="transfer-empty">
                  Chưa có sản phẩm trong phiếu
                </td>
              </tr>
            ) : (
              getReceiptItems().map((item, index) => {
                const expectedQty = Number(
                  item.expectedQuantity ||
                    item.expected_quantity ||
                    item.quantity ||
                    item.plannedQuantity ||
                    0
                );

                const receivedQty = Number(
                  item.receivedQuantity ||
                    item.received_quantity ||
                    item.actualQuantity ||
                    item.checkedQuantity ||
                    0
                );

                const difference = receivedQty - expectedQty;

                return (
                  <tr key={item.id || index}>
                    <td>{index + 1}</td>

                    <td className="code-cell">
                      {item.productSku || item.sku || item.productCode || "-"}
                    </td>

                    <td>
                      {item.productBarcode ||
                        item.barcode ||
                        item.product?.barcode ||
                        "-"}
                    </td>

                    <td className="strong-cell">
                      {item.productName ||
                        item.name ||
                        item.product?.name ||
                        "Chưa có tên sản phẩm"}
                    </td>

                    <td className="quantity-cell">{expectedQty}</td>
                    <td className="quantity-cell">{receivedQty}</td>

                    <td>
                      <span
                        className={`difference-badge ${
                          difference === 0
                            ? "equal"
                            : difference > 0
                            ? "over"
                            : "missing"
                        }`}
                      >
                        {difference}
                      </span>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>

    <div className="goods-detail-card">
      <div className="goods-card-title">
        <h2>Theo dõi kiểm nhận</h2>
        <span>Nghiệp vụ</span>
      </div>

      <div className="business-status-box">
        <p>
          <strong>Trạng thái hiện tại:</strong>{" "}
          <span
            className={`goods-status-badge ${
              receipt?.status === "COMPLETED"
                ? "completed"
                : receipt?.status === "APPROVED"
                ? "approved"
                : "draft"
            }`}
          >
            {receipt?.status || "DRAFT"}
          </span>
        </p>


      </div>
    </div>
  </div>
);
}

export default GoodsReceiptDetail;