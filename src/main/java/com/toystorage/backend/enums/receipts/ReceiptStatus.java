package com.toystorage.backend.enums.receipts;

public enum ReceiptStatus {

    DRAFT,          // Trưởng cửa hàng tạo phiếu

    REQUESTED,      // Gửi yêu cầu nhập hàng

    ORDERING,       // Kinh doanh đang order supplier

    DELIVERING,     // Supplier đang giao hàng

    ARRIVED,        // Hàng đã đến kho tổng

    CHECKING,       // Kho đang kiểm hàng

    CHECKED,        // Kiểm hàng xong

    PUTAWAY,        // Đang đưa hàng lên kệ

    COMPLETED,      // Hoàn tất nhập kho

    DISCREPANCY,    // Có lệch số lượng

    CANCELLED
}