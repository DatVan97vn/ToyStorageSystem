import { useEffect } from "react";
import { Html5QrcodeScanner } from "html5-qrcode";

function WarehouseScan() {
  useEffect(() => {
    const scanner = new Html5QrcodeScanner(
      "reader",
      {
        fps: 10,
        qrbox: 250,
      },
      false
    );

    scanner.render(
      (decodedText) => {
        alert(`Đã quét được: ${decodedText}`);

        scanner.clear();
      },
      (error) => {
        console.log(error);
      }
    );

    return () => {
      scanner.clear().catch(() => {});
    };
  }, []);

  return (
    <div className="page-container">
      <h2>Quét QR Phiếu Nhập</h2>

      <p>
        Đưa camera vào mã QR trên phiếu kiểm hàng.
      </p>

      <div
        id="reader"
        style={{
          width: "500px",
          maxWidth: "100%",
          marginTop: "20px",
        }}
      />
    </div>
  );
}

export default WarehouseScan;