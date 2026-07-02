import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import QRCode from "qrcode";
import ExcelJS from "exceljs";
import { saveAs } from "file-saver";
import { getProducts } from "../../api/business/productApi";

import {
  approveTransfer,
  completeTransfer,
  getTransferById,
} from "../../api/business/transferApi";

function TransferDetail() {
  const [products, setProducts] = useState([]);
  const { id } = useParams();
  const [transfer, setTransfer] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadTransfer = async () => {
    try {
      const transferRes = await getTransferById(id);
      const productRes = await getProducts();

      setTransfer(transferRes.data);
      setProducts(productRes.data);
    } catch (error) {
      console.error("Lỗi lấy chi tiết phiếu:", error);
      alert("Không lấy được chi tiết phiếu");
    } finally {
      setLoading(false);
    }
  };
  const findProductByItem = (item) => {
    return products.find(
      (product) =>
        Number(product.id) === Number(item.productId) ||
        String(product.sku) === String(item.sku)
    );
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

  const getBaseTransferCode = (item = transfer) => {
    return item?.transferCode || item?.code || `TRF-${item?.id || id}`;
  };

  const getExportCode = (item = transfer) => {
    const baseCode = getBaseTransferCode(item);

    return baseCode.startsWith("TRF-")
      ? baseCode.replace("TRF-", "XK-")
      : `XK-${baseCode}`;
  };

  const getReceiveCode = (item = transfer) => {
    const baseCode = getBaseTransferCode(item);

    return baseCode.startsWith("TRF-")
      ? baseCode.replace("TRF-", "NK-")
      : `NK-${baseCode}`;
  };

  const getWarehouseName = (warehouse, fallback = "Chưa cập nhật") => {
    if (!warehouse) return fallback;
    if (typeof warehouse === "string") return warehouse;

    return warehouse.warehouseName || warehouse.name || warehouse.code || fallback;
  };

  const getFromWarehouseName = (item = transfer) => {
    return (
      item?.fromWarehouseName ||
      item?.sourceWarehouseName ||
      getWarehouseName(item?.fromWarehouse || item?.sourceWarehouse)
    );
  };

  const getToWarehouseName = (item = transfer) => {
    return (
      item?.toWarehouseName ||
      item?.destinationWarehouseName ||
      getWarehouseName(item?.toWarehouse || item?.destinationWarehouse)
    );
  };

  const getTransferItems = () => {
    return Array.isArray(transfer?.items) ? transfer.items : [];
  };

  const getItemCode = (item) => {
    const product = findProductByItem(item);

    return item.productCode || item.sku || product?.sku || item.productId || "";
  };


  const getItemBarcode = (item) => {
  const product = findProductByItem(item);

    return (
      item.barcode ||
      item.productBarcode ||
      item.product?.barcode ||
      product?.barcode ||
      ""
    );
  };

  const getItemName = (item) => {
    const product = findProductByItem(item);

    return item.productName || item.name || product?.name || "Chưa có tên sản phẩm";
  };

  const getItemUnit = (item) => {
    const product = findProductByItem(item);

    return item.unit || item.productUnit || product?.unit || "";
  };

  const getItemQuantity = (item) => {
    return Number(
      item.requestedQuantity || item.quantity || item.requestedQty || 0
    );
  };

  const getItemPrice = (item) => {
    const product = findProductByItem(item);

    return Number(
      item.salePrice ||
        item.sellingPrice ||
        item.unitPrice ||
        item.price ||
        item.importPrice ||
        product?.salePrice ||
        product?.sellingPrice ||
        product?.unitPrice ||
        product?.price ||
        product?.importPrice ||
        0
    );
  };

  const formatDate = (value) => {
    if (!value) return "Chưa hoàn tất";
    return new Date(value).toLocaleString("vi-VN");
  };

  const handleExportExcel = async () => {
    if (!transfer) {
      alert("Chưa có dữ liệu phiếu điều kho");
      return;
    }

    const transferItems = getTransferItems();
    const exportCode = getExportCode();
    const receiveCode = getReceiveCode();
    const fromWarehouseName = getFromWarehouseName();
    const toWarehouseName = getToWarehouseName();

    const totalAmount = transferItems.reduce((sum, item) => {
      return sum + getItemQuantity(item) * getItemPrice(item);
    }, 0);

    const workbook = new ExcelJS.Workbook();
    const worksheet = workbook.addWorksheet("Phieu dieu kho");

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
  { width: 14 },  // A
  { width: 22 },  // B
  { width: 22 },  // C
  { width: 34 },  // D
  { width: 12 },  // E
  { width: 14 },  // F
  { width: 22 },  // G
  { width: 20 },  // H
  { width: 16 },  // I
  { width: 22 },  // J
];

    worksheet.mergeCells("A1:J1");
    worksheet.getRow(1).height = 32;
    worksheet.getRow(2).height = 26;
    worksheet.getRow(3).height = 12;
    worksheet.getRow(4).height = 28;
    worksheet.getCell("A1").value =
      "LỆNH ĐIỀU ĐỘNG / PHIẾU XUẤT ĐIỀU KHO";
    worksheet.getCell("A1").font = { bold: true, size: 18 };
    worksheet.getCell("A1").alignment = {
      horizontal: "center",
      vertical: "middle",
    };

    worksheet.mergeCells("A2:J2");
    worksheet.getCell("A2").value = `Ngày lập: ${formatDate(
      transfer.createdAt
    )}`;
    worksheet.getCell("A2").font = { bold: true, size: 12 };
    worksheet.getCell("A2").alignment = { horizontal: "center" };

    worksheet.mergeCells("A4:E4");
    worksheet.getCell("A4").value = "THÔNG TIN BÊN XUẤT";
    worksheet.getCell("A4").font = { bold: true, size: 13 };

    worksheet.mergeCells("F4:J4");
    worksheet.getCell("F4").value = "THÔNG TIN BÊN NHẬN";
    worksheet.getCell("F4").font = { bold: true, size: 13 };

    worksheet.getRow(5).height = 24;
    worksheet.getRow(6).height = 24;
    worksheet.getRow(7).height = 24;
    worksheet.getRow(8).height = 24;

    worksheet.getCell("A5").value = "Mã xuất:";
    worksheet.getCell("B5").value = exportCode;
    worksheet.mergeCells("B5:C5");

    worksheet.getCell("A6").value = "Đơn vị xuất:";
    worksheet.getCell("B6").value = fromWarehouseName;
    worksheet.mergeCells("B6:C6");

    worksheet.getCell("A7").value = "Số CT xuất:";
    worksheet.getCell("B7").value = exportCode;
    worksheet.mergeCells("B7:C7");

    worksheet.getCell("A8").value = "Ngày xuất:";
    worksheet.getCell("B8").value = formatDate(transfer.createdAt);
    worksheet.mergeCells("B8:C8");

    worksheet.getCell("F5").value = "Mã nhận:";
    worksheet.getCell("G5").value = receiveCode;
    worksheet.mergeCells("G5:H5");

    worksheet.getCell("F6").value = "Đơn vị nhận:";
    worksheet.getCell("G6").value = toWarehouseName;
    worksheet.mergeCells("G6:H6");

    worksheet.getCell("F7").value = "Số CT nhận:";
    worksheet.getCell("G7").value = receiveCode;
    worksheet.mergeCells("G7:H7");

    worksheet.getCell("F8").value = "Ngày nhận:";
    worksheet.getCell("G8").value = transfer.completedAt
      ? formatDate(transfer.completedAt)
      : "Chưa hoàn tất";
    worksheet.mergeCells("G8:H8");

    worksheet.getCell("D5").value = "QR xuất";
    worksheet.getCell("I5").value = "QR nhận";
    worksheet.getCell("D5").font = { bold: true };
    worksheet.getCell("I5").font = { bold: true };

    const exportQrDataUrl = await QRCode.toDataURL(
      JSON.stringify({
        type: "TRANSFER_EXPORT",
        id: transfer.id,
        code: exportCode,
        transferCode: getBaseTransferCode(),
        warehouse: fromWarehouseName,
      })
    );

    const receiveQrDataUrl = await QRCode.toDataURL(
      JSON.stringify({
        type: "TRANSFER_RECEIVE",
        id: transfer.id,
        code: receiveCode,
        transferCode: getBaseTransferCode(),
        warehouse: toWarehouseName,
      })
    );

    const exportQrImageId = workbook.addImage({
      base64: exportQrDataUrl.replace(/^data:image\/png;base64,/, ""),
      extension: "png",
    });

    const receiveQrImageId = workbook.addImage({
      base64: receiveQrDataUrl.replace(/^data:image\/png;base64,/, ""),
      extension: "png",
    });

    worksheet.addImage(exportQrImageId, {
      tl: { col: 3.15, row: 4.25 },
      ext: { width: 105, height: 105 },
    });

    worksheet.addImage(receiveQrImageId, {
      tl: { col: 8.15, row: 4.25 },
      ext: { width: 105, height: 105 },
    });

    const headerRow = worksheet.getRow(11);
    headerRow.values = [
      "STT",
      "Mã sản phẩm",
      "Barcode",
      "Tên sản phẩm",
      "ĐVT",
      "SL",
      "Giá bán",
      "Thành tiền",
      "SL thực tế",
      "Ghi chú",
    ];

    headerRow.font = { bold: true };
    headerRow.fill = {
      type: "pattern",
      pattern: "solid",
      fgColor: { argb: "FFFCEFE3" },
    };

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
        "",
        "",
      ]);
    } else {
      transferItems.forEach((item, index) => {
        const quantity = getItemQuantity(item);
        const price = getItemPrice(item);

        worksheet.addRow([
          index + 1,
          getItemCode(item),
          getItemBarcode(item),
          getItemName(item),
          getItemUnit(item),
          quantity,
          price,
          quantity * price,
          "",
          "",
        ]);
      });
    }

    const totalRowIndex = 12 + Math.max(transferItems.length, 1);

    worksheet.mergeCells(`A${totalRowIndex}:G${totalRowIndex}`);
    worksheet.getCell(`A${totalRowIndex}`).value = "TỔNG TIỀN";
    worksheet.getCell(`A${totalRowIndex}`).font = { bold: true };
    worksheet.getCell(`A${totalRowIndex}`).alignment = {
      horizontal: "right",
    };

    worksheet.getCell(`H${totalRowIndex}`).value = totalAmount;
    worksheet.getCell(`H${totalRowIndex}`).font = { bold: true };

    const signRow = totalRowIndex + 3;

    worksheet.mergeCells(`A${signRow}:C${signRow}`);
    worksheet.mergeCells(`D${signRow}:F${signRow}`);
    worksheet.mergeCells(`H${signRow}:J${signRow}`);

    worksheet.getCell(`A${signRow}`).value = "Người lập phiếu";
    worksheet.getCell(`D${signRow}`).value = "Người xuất kho";
    worksheet.getCell(`H${signRow}`).value = "Người nhận kho";

    worksheet.mergeCells(`A${signRow + 3}:C${signRow + 3}`);
    worksheet.mergeCells(`D${signRow + 3}:F${signRow + 3}`);
    worksheet.mergeCells(`H${signRow + 3}:J${signRow + 3}`);

    worksheet.getCell(`A${signRow + 3}`).value = "Ký tên";
    worksheet.getCell(`D${signRow + 3}`).value = "Ký tên";
    worksheet.getCell(`H${signRow + 3}`).value = "Ký tên";



    for (let rowIndex = 1; rowIndex <= signRow + 3; rowIndex++) {
      worksheet.getRow(rowIndex).eachCell((cell) => {
        cell.alignment = {
          vertical: "middle",
          horizontal: "left",
          wrapText: false,
          shrinkToFit: true,
        };
      });
    }
        [
      `A${signRow}`,
      `D${signRow}`,
      `H${signRow}`,
      `A${signRow + 3}`,
      `D${signRow + 3}`,
      `H${signRow + 3}`,
    ].forEach((cellAddress) => {
      worksheet.getCell(cellAddress).font = { bold: true };
      worksheet.getCell(cellAddress).alignment = {
        horizontal: "center",
        vertical: "middle",
        wrapText: false,
      };
    });
    worksheet.getCell("A1").alignment = {
      horizontal: "center",
      vertical: "middle",
    };

    worksheet.getCell("A2").alignment = {
      horizontal: "center",
      vertical: "middle",
    };

    ["A5", "A6", "A7", "A8", "F5", "F6", "F7", "F8"].forEach((cellAddress) => {
      worksheet.getCell(cellAddress).font = { bold: true };
      worksheet.getCell(cellAddress).alignment = {
        horizontal: "left",
        vertical: "middle",
        wrapText: false,
        shrinkToFit: true,
      };
    });

    for (let rowIndex = 11; rowIndex <= totalRowIndex; rowIndex++) {
      worksheet.getRow(rowIndex).eachCell((cell) => {
        cell.border = {
          top: { style: "thin" },
          left: { style: "thin" },
          bottom: { style: "thin" },
          right: { style: "thin" },
        };
      });
    }

    worksheet.getColumn(7).numFmt = "#,##0";
    worksheet.getColumn(8).numFmt = "#,##0";

    const buffer = await workbook.xlsx.writeBuffer();

    saveAs(
      new Blob([buffer], {
        type:
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      }),
      `${exportCode}_${receiveCode}.xlsx`
    );
  };

  if (loading) return <p>Đang tải chi tiết phiếu...</p>;

  if (!transfer) return <p>Không tìm thấy phiếu</p>;

  return (
    <div className="page-container transfer-detail-page">
      <div className="transfer-detail-header">
        <div>
          <h1>Chi tiết phiếu điều kho</h1>
          <p>
            Xem thông tin phiếu, danh sách sản phẩm và thực hiện duyệt hoặc hoàn
            tất phiếu.
          </p>
        </div>

        <button
          type="button"
          className="transfer-excel-btn"
          onClick={handleExportExcel}
        >
          Xuất Excel
        </button>
      </div>

      <div className="transfer-detail-card">
        <div className="transfer-card-title">
          <h2>Thông tin phiếu</h2>

          <span
            className={`detail-status-badge ${
              transfer?.status === "COMPLETED"
                ? "completed"
                : transfer?.status === "APPROVED"
                ? "approved"
                : "draft"
            }`}
          >
            {transfer?.status || "DRAFT"}
          </span>
        </div>

        <div className="transfer-info-grid">
          <div className="info-item">
            <span>ID</span>
            <strong>{transfer?.id}</strong>
          </div>

          <div className="info-item">
            <span>Mã xuất</span>
            <strong>{getExportCode()}</strong>
          </div>

          <div className="info-item">
            <span>Mã nhận</span>
            <strong>{getReceiveCode()}</strong>
          </div>

          <div className="info-item">
            <span>Kho xuất</span>
            <strong>{getFromWarehouseName()}</strong>
          </div>

          <div className="info-item">
            <span>Kho nhập</span>
            <strong>{getToWarehouseName()}</strong>
          </div>

          <div className="info-item">
            <span>Ngày tạo</span>
            <strong>{transfer?.createdAt}</strong>
          </div>

          <div className="info-item">
            <span>Ngày hoàn tất</span>
            <strong>{transfer?.completedAt || "Chưa hoàn tất"}</strong>
          </div>
        </div>
      </div>

      <div className="transfer-detail-card">
        <div className="transfer-card-title">
          <h2>Danh sách sản phẩm trong phiếu</h2>
          <span>{transfer?.items?.length || 0} sản phẩm</span>
        </div>

        <div className="transfer-table-scroll">
          <table className="transfer-items-table">
            <thead>
              <tr>
                <th>STT</th>
                <th>Mã sản phẩm</th>
                <th>Tên sản phẩm</th>
                <th>Số lượng</th>
              </tr>
            </thead>

            <tbody>
              {!transfer?.items || transfer.items.length === 0 ? (
                <tr>
                  <td colSpan="4" className="transfer-empty">
                    Chưa có sản phẩm trong phiếu
                  </td>
                </tr>
              ) : (
                transfer.items.map((item, index) => (
                  <tr key={item.id || index}>
                    <td>{index + 1}</td>
                    <td className="code-cell">{getItemCode(item)}</td>
                    <td className="strong-cell">{getItemName(item)}</td>
                    <td className="quantity-cell">{getItemQuantity(item)}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

     <div className="transfer-detail-card">
  <div className="transfer-card-title">
    <h2>Theo dõi xử lý</h2>

  </div>

  <div className="business-status-box">
      <p>
        <strong>Trạng thái hiện tại:</strong>{" "}
        <span
          className={`detail-status-badge ${
            transfer?.status === "COMPLETED"
              ? "completed"
              : transfer?.status === "APPROVED"
              ? "approved"
              : "draft"
          }`}
        >
          {transfer?.status || "DRAFT"}
        </span>
      </p>


    </div>
  </div>
    </div>
  );
}

export default TransferDetail;