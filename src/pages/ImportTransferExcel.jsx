import { useEffect, useMemo, useState } from "react";
import * as XLSX from "xlsx";
import { getProducts } from "../api/productApi";
import { getWarehouses } from "../api/warehouseApi";
import { createTransfer } from "../api/transferApi";

function ImportTransferExcel() {
  const [products, setProducts] = useState([]);
  const [warehouses, setWarehouses] = useState([]);
  const [rows, setRows] = useState([]);
  const [errors, setErrors] = useState([]);

  useEffect(() => {
    const loadData = async () => {
      const productRes = await getProducts();
      const warehouseRes = await getWarehouses();

      setProducts(productRes.data || []);
      setWarehouses(warehouseRes.data || []);
    };

    loadData();
  }, []);

  const productByBarcode = useMemo(() => {
    const map = new Map();
    products.forEach((p) => map.set(String(p.barcode), p));
    return map;
  }, [products]);

  const warehouseByCode = useMemo(() => {
    const map = new Map();
    warehouses.forEach((w) => map.set(String(w.code), w));
    return map;
  }, [warehouses]);

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const data = await file.arrayBuffer();
    const workbook = XLSX.read(data);
    const sheetName = workbook.SheetNames[0];
    const sheet = workbook.Sheets[sheetName];

    const jsonRows = XLSX.utils.sheet_to_json(sheet, {
      defval: "",
    });

    const parsedRows = jsonRows.map((row, index) => ({
      rowNumber: index + 2,
      barcode: String(row.barcode || row.Barcode || row.BARCODE || "").trim(),
      quantity: Number(row.quantity || row.Quantity || row.QUANTITY || 0),
      fromWarehouseCode: String(
        row.fromWarehouseCode ||
          row.FromWarehouseCode ||
          row.FROM_WAREHOUSE_CODE ||
          ""
      ).trim(),
      toWarehouseCode: String(
        row.toWarehouseCode ||
          row.ToWarehouseCode ||
          row.TO_WAREHOUSE_CODE ||
          ""
      ).trim(),
    }));

    setRows(parsedRows);
    validateRows(parsedRows);
  };

  const validateRows = (dataRows) => {
    const newErrors = [];

    dataRows.forEach((row) => {
      if (!row.barcode) {
        newErrors.push(`Dòng ${row.rowNumber}: Thiếu barcode`);
      } else if (!productByBarcode.has(row.barcode)) {
        newErrors.push(`Dòng ${row.rowNumber}: Barcode không tồn tại`);
      }

      if (!row.quantity || row.quantity <= 0) {
        newErrors.push(`Dòng ${row.rowNumber}: Số lượng không hợp lệ`);
      }

      if (!row.fromWarehouseCode) {
        newErrors.push(`Dòng ${row.rowNumber}: Thiếu mã kho xuất`);
      } else if (!warehouseByCode.has(row.fromWarehouseCode)) {
        newErrors.push(`Dòng ${row.rowNumber}: Mã kho xuất không tồn tại`);
      }

      if (!row.toWarehouseCode) {
        newErrors.push(`Dòng ${row.rowNumber}: Thiếu mã kho nhập`);
      } else if (!warehouseByCode.has(row.toWarehouseCode)) {
        newErrors.push(`Dòng ${row.rowNumber}: Mã kho nhập không tồn tại`);
      }

      if (
        row.fromWarehouseCode &&
        row.toWarehouseCode &&
        row.fromWarehouseCode === row.toWarehouseCode
      ) {
        newErrors.push(`Dòng ${row.rowNumber}: Kho xuất và kho nhập bị trùng`);
      }
    });

    setErrors(newErrors);
  };

  const groupedPayloads = useMemo(() => {
    const map = new Map();

    rows.forEach((row) => {
      const product = productByBarcode.get(row.barcode);
      const fromWarehouse = warehouseByCode.get(row.fromWarehouseCode);
      const toWarehouse = warehouseByCode.get(row.toWarehouseCode);

      if (!product || !fromWarehouse || !toWarehouse || row.quantity <= 0) {
        return;
      }

      const key = `${fromWarehouse.id}-${toWarehouse.id}`;

      if (!map.has(key)) {
        map.set(key, {
          fromWarehouseId: fromWarehouse.id,
          toWarehouseId: toWarehouse.id,
          items: [],
        });
      }

      map.get(key).items.push({
        productId: product.id,
        quantity: row.quantity,
      });
    });

    return Array.from(map.values());
  }, [rows, productByBarcode, warehouseByCode]);

  const handleCreateTransfers = async () => {
    if (rows.length === 0) {
      alert("Vui lòng chọn file Excel");
      return;
    }

    validateRows(rows);

    if (errors.length > 0) {
      alert("File còn lỗi, vui lòng kiểm tra lại");
      return;
    }

    if (groupedPayloads.length === 0) {
      alert("Không có dữ liệu hợp lệ để tạo phiếu");
      return;
    }

    try {
      for (const payload of groupedPayloads) {
        console.log("PAYLOAD IMPORT:", payload);
        await createTransfer(payload);
      }

      alert(`Tạo thành công ${groupedPayloads.length} phiếu điều kho`);
      setRows([]);
      setErrors([]);
    } catch (error) {
      console.error("Lỗi import Excel:", error);
      alert("Tạo phiếu từ Excel thất bại");
    }
  };

  return (
    <div style={pageStyle}>
      <h1>Import Excel tạo phiếu điều kho</h1>
      <p style={{ color: "#9ca3af" }}>
        Business upload Excel gồm barcode, quantity, mã kho xuất và mã kho nhập.
      </p>

      <div style={sectionStyle}>
        <h3>Định dạng file Excel</h3>
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thStyle}>barcode</th>
              <th style={thStyle}>quantity</th>
              <th style={thStyle}>fromWarehouseCode</th>
              <th style={thStyle}>toWarehouseCode</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td style={tdStyle}>893000000001</td>
              <td style={tdStyle}>5</td>
              <td style={tdStyle}>WH001</td>
              <td style={tdStyle}>STORE001</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div style={sectionStyle}>
        <input type="file" accept=".xlsx,.xls" onChange={handleFileChange} />

        <button style={buttonStyle} type="button" onClick={handleCreateTransfers}>
          📄 Tạo phiếu từ Excel
        </button>
      </div>

      {errors.length > 0 && (
        <div style={errorBoxStyle}>
          <h3>Lỗi dữ liệu</h3>
          {errors.map((error, index) => (
            <div key={index}>- {error}</div>
          ))}
        </div>
      )}

      <div style={sectionStyle}>
        <h3>Preview dữ liệu</h3>

        {rows.length === 0 ? (
          <p>Chưa có dữ liệu Excel</p>
        ) : (
          <table style={tableStyle}>
            <thead>
              <tr>
                <th style={thStyle}>Dòng</th>
                <th style={thStyle}>Barcode</th>
                <th style={thStyle}>Sản phẩm</th>
                <th style={thStyle}>Số lượng</th>
                <th style={thStyle}>Kho xuất</th>
                <th style={thStyle}>Kho nhập</th>
              </tr>
            </thead>

            <tbody>
              {rows.map((row) => {
                const product = productByBarcode.get(row.barcode);
                const fromWarehouse = warehouseByCode.get(row.fromWarehouseCode);
                const toWarehouse = warehouseByCode.get(row.toWarehouseCode);

                return (
                  <tr key={row.rowNumber}>
                    <td style={tdStyle}>{row.rowNumber}</td>
                    <td style={tdStyle}>{row.barcode}</td>
                    <td style={tdStyle}>
                      {product ? product.name : "Không tìm thấy"}
                    </td>
                    <td style={tdStyle}>{row.quantity}</td>
                    <td style={tdStyle}>
                      {fromWarehouse ? fromWarehouse.name : "Không tìm thấy"}
                    </td>
                    <td style={tdStyle}>
                      {toWarehouse ? toWarehouse.name : "Không tìm thấy"}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

const pageStyle = {
  maxWidth: "1200px",
  margin: "0 auto",
};

const sectionStyle = {
  marginTop: "24px",
};

const buttonStyle = {
  marginLeft: "15px",
  padding: "10px 16px",
  borderRadius: "8px",
  border: "none",
  background: "#3b82f6",
  color: "white",
  fontWeight: "bold",
  cursor: "pointer",
};

const errorBoxStyle = {
  marginTop: "24px",
  padding: "15px",
  borderRadius: "8px",
  background: "#7f1d1d",
  color: "white",
};

const tableStyle = {
  width: "100%",
  borderCollapse: "collapse",
};

const thStyle = {
  border: "1px solid #d1d5db",
  padding: "10px",
  textAlign: "left",
};

const tdStyle = {
  border: "1px solid #d1d5db",
  padding: "10px",
};

export default ImportTransferExcel;