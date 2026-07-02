import { useEffect, useMemo, useState } from "react";
import * as XLSX from "xlsx";

import { getProducts } from "../../api/business/productApi";
import { getWarehouses } from "../../api/business/warehouseApi";
import { createTransfer } from "../../api/business/transferApi";

function ImportTransferExcel() {
  const [products, setProducts] = useState([]);
  const [warehouses, setWarehouses] = useState([]);

  const [fromWarehouseId, setFromWarehouseId] = useState("");
  const [toWarehouseId, setToWarehouseId] = useState("");

  const [rows, setRows] = useState([]);
  const [errors, setErrors] = useState([]);
  const [fileName, setFileName] = useState("");

  useEffect(() => {
    const loadData = async () => {
      try {
        const productRes = await getProducts();
        const warehouseRes = await getWarehouses();

        setProducts(productRes.data || []);
        setWarehouses(warehouseRes.data || []);
      } catch (error) {
        console.error("Lỗi tải dữ liệu import:", error);
        alert("Không tải được danh sách sản phẩm hoặc kho");
      }
    };

    loadData();
  }, []);

  const productByBarcode = useMemo(() => {
    const map = new Map();

    products.forEach((product) => {
      if (product.barcode) {
        map.set(String(product.barcode).trim(), product);
      }
    });

    return map;
  }, [products]);

  const selectedFromWarehouse = useMemo(() => {
    return warehouses.find(
      (warehouse) => String(warehouse.id) === String(fromWarehouseId)
    );
  }, [warehouses, fromWarehouseId]);

  const selectedToWarehouse = useMemo(() => {
    return warehouses.find(
      (warehouse) => String(warehouse.id) === String(toWarehouseId)
    );
  }, [warehouses, toWarehouseId]);

  const validateRows = (dataRows) => {
    const newErrors = [];

    if (!fromWarehouseId) {
      newErrors.push("Vui lòng chọn kho xuất");
    }

    if (!toWarehouseId) {
      newErrors.push("Vui lòng chọn kho nhập");
    }

    if (
      fromWarehouseId &&
      toWarehouseId &&
      String(fromWarehouseId) === String(toWarehouseId)
    ) {
      newErrors.push("Kho xuất và kho nhập không được trùng nhau");
    }

    if (dataRows.length === 0) {
      newErrors.push("File Excel chưa có dữ liệu");
    }

    dataRows.forEach((row) => {
      if (!row.ma) {
        newErrors.push(`Dòng ${row.rowNumber}: Thiếu mã barcode`);
      } else if (!productByBarcode.has(row.ma)) {
        newErrors.push(`Dòng ${row.rowNumber}: Barcode ${row.ma} không tồn tại`);
      }

      if (!row.sl || row.sl <= 0) {
        newErrors.push(`Dòng ${row.rowNumber}: Số lượng không hợp lệ`);
      }
    });

    setErrors(newErrors);
    return newErrors;
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];

    if (!file) {
      setRows([]);
      setErrors([]);
      setFileName("");
      return;
    }

    try {
      setFileName(file.name);

      const data = await file.arrayBuffer();
      const workbook = XLSX.read(data);
      const sheetName = workbook.SheetNames[0];
      const sheet = workbook.Sheets[sheetName];

      const jsonRows = XLSX.utils.sheet_to_json(sheet, {
        defval: "",
      });

      const parsedRows = jsonRows
        .map((row, index) => {
          const ma = String(
            row.ma || row.Ma || row.MA || row["mã"] || row["Mã"] || ""
          ).trim();

          const sl = Number(
            row.sl ||
              row.SL ||
              row.Sl ||
              row["số lượng"] ||
              row["Số lượng"] ||
              0
          );

          return {
            rowNumber: index + 2,
            ma,
            sl,
          };
        })
        .filter((row) => row.ma || row.sl);

      setRows(parsedRows);
      validateRows(parsedRows);
    } catch (error) {
      console.error("Lỗi đọc file Excel:", error);
      alert("Không đọc được file Excel");
      setRows([]);
      setErrors(["Không đọc được file Excel"]);
    }
  };

  const previewRows = useMemo(() => {
    return rows.map((row) => {
      const product = productByBarcode.get(row.ma);

      return {
        ...row,
        product,
      };
    });
  }, [rows, productByBarcode]);

  const handleCreateTransfers = async () => {
    const newErrors = validateRows(rows);

    if (newErrors.length > 0) {
      alert("File còn lỗi, vui lòng kiểm tra lại");
      return;
    }

    const items = rows.map((row) => {
      const product = productByBarcode.get(row.ma);

      return {
        productId: product.id,
        quantity: row.sl,
      };
    });

    const payload = {
      fromWarehouseId: Number(fromWarehouseId),
      toWarehouseId: Number(toWarehouseId),
      items,
    };

    try {
      console.log("PAYLOAD IMPORT:", payload);

      await createTransfer(payload);

      alert("Tạo phiếu điều kho từ Excel thành công");

      setRows([]);
      setErrors([]);
      setFileName("");
      setFromWarehouseId("");
      setToWarehouseId("");
    } catch (error) {
      console.error("Lỗi import Excel:", error);
      alert("Tạo phiếu từ Excel thất bại");
    }
  };

  return (
    <div className="page-container import-excel-page">
      <div className="import-header">
        <h1>Import Excel tạo phiếu điều kho</h1>
        <p>
          Upload file Excel gồm 2 cột <strong>ma</strong> và{" "}
          <strong>sl</strong>. Kho xuất và kho nhập được chọn trực tiếp trên hệ
          thống.
        </p>
      </div>

      <div className="import-card">
        <div className="import-card-title">
          <h2>Thông tin điều kho</h2>
          
        </div>

        <div className="import-form-grid">
          <div className="form-group">
            <label>Kho xuất</label>
            <select
              value={fromWarehouseId}
              onChange={(event) => {
                setFromWarehouseId(event.target.value);
                setErrors([]);
              }}
            >
              <option value="">-- Chọn kho xuất --</option>
              {warehouses.map((warehouse) => (
                <option key={warehouse.id} value={warehouse.id}>
                  {warehouse.code} - {warehouse.name} ({warehouse.type})
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Kho nhập</label>
            <select
              value={toWarehouseId}
              onChange={(event) => {
                setToWarehouseId(event.target.value);
                setErrors([]);
              }}
            >
              <option value="">-- Chọn kho nhập --</option>
              {warehouses
                .filter(
                  (warehouse) =>
                    String(warehouse.id) !== String(fromWarehouseId)
                )
                .map((warehouse) => (
                  <option key={warehouse.id} value={warehouse.id}>
                    {warehouse.code} - {warehouse.name} ({warehouse.type})
                  </option>
                ))}
            </select>
          </div>
        </div>

        {(selectedFromWarehouse || selectedToWarehouse) && (
          <div className="import-selected-info">
            <p>
              <strong>Kho xuất:</strong>{" "}
              {selectedFromWarehouse
                ? `${selectedFromWarehouse.code} - ${selectedFromWarehouse.name}`
                : "Chưa chọn"}
            </p>
            <p>
              <strong>Kho nhập:</strong>{" "}
              {selectedToWarehouse
                ? `${selectedToWarehouse.code} - ${selectedToWarehouse.name}`
                : "Chưa chọn"}
            </p>
          </div>
        )}
      </div>

      <div className="import-card">
        <div className="import-card-title">
          <h2>File Excel</h2>

        </div>

        <div className="excel-format-box">
          <p>Định dạng file Excel</p>

          <table>
            <thead>
              <tr>
                <th>ma</th>
                <th>sl</th>
              </tr>
            </thead>

            <tbody>
              <tr>
                <td>9781108907194</td>
                <td>4</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div className="file-row">
          <label className="file-input-label">
            Chọn file Excel
            <input
              type="file"
              accept=".xlsx,.xls"
              onChange={handleFileChange}
            />
          </label>

          <span className="file-name">
            {fileName || "Chưa có file nào được chọn"}
          </span>
        </div>
      </div>

      {errors.length > 0 && (
        <div className="import-error-box">
          <h3>Lỗi dữ liệu</h3>
          {errors.map((error, index) => (
            <div key={index}>- {error}</div>
          ))}
        </div>
      )}

      <div className="import-card">
        <div className="import-card-title">
          <h2>Preview dữ liệu</h2>
          <span>{rows.length} dòng</span>
        </div>

        <div className="transfer-table-scroll">
          <table className="transfer-items-table">
            <thead>
              <tr>
                <th>Dòng</th>
                <th>ma</th>
                <th>sl</th>
                <th>Sản phẩm</th>
                <th>SKU</th>
                <th>Đơn vị</th>
              </tr>
            </thead>

            <tbody>
              {previewRows.length === 0 ? (
                <tr>
                  <td colSpan="6" className="transfer-empty">
                    Chưa có dữ liệu Excel
                  </td>
                </tr>
              ) : (
                previewRows.map((row) => (
                  <tr key={row.rowNumber}>
                    <td>{row.rowNumber}</td>
                    <td className="code-cell">{row.ma}</td>
                    <td className="quantity-cell">{row.sl}</td>
                    <td className={row.product ? "strong-cell" : "error-cell"}>
                      {row.product ? row.product.name : "Không tìm thấy"}
                    </td>
                    <td>{row.product?.sku || "-"}</td>
                    <td>
                      {row.product?.unit ? (
                        <span className="unit-badge">{row.product.unit}</span>
                      ) : (
                        "-"
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        <div className="import-submit-row">
          <button
            type="button"
            className="import-submit-btn"
            onClick={handleCreateTransfers}
          >
            Tạo phiếu từ Excel
          </button>
        </div>
      </div>
    </div>
  );
}

export default ImportTransferExcel;