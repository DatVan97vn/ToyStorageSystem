package com.toystorage.backend.enums.transfers;

public enum TransferStatus {

    DRAFT,          // mới tạo phiếu

    APPROVED,       // trưởng kho duyệt

    PICKING,        // nhân viên đang lấy hàng

    PACKING,        // đang đóng thùng

    SHIPPED,        // đã xuất kho

    RECEIVING,      // kho nhận đang kiểm

    COMPLETED,      // hoàn tất

    CANCELLED
}