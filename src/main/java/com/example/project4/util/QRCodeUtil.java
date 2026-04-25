package com.example.project4.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class QRCodeUtil {

//    Ở đây dùng static vì QR là tool không cần lưu trạng thái và dữ liệu
    public static String generateQRImage(String text) throws Exception {
//        Tạo QRCodeWriter
        QRCodeWriter writer = new QRCodeWriter();
//        Encode text sang QR data 250 là kích cỡ QR 250px x 250px
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250);
//          Lưu ảnh tạm trong ram để trước khi xử lý tiếp
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // chuyển từ QR data sang PNG
        MatrixToImageWriter.writeToStream(matrix, "PNG", stream);
//      dùng để chuyển ảnh QR (dạng byte) thành chuỗi Base64 để có thể gửi và hiển thị trên web/API
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }
}