package com.toystorage.backend.models.receipts;

import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.products.Products;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "goods_receipt_scans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id")
    private GoodsReceipt goodsReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_item_id")
    private GoodsReceiptItem goodsReceiptItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products product;

    @Column(nullable = false)
    private String barcode;

    @Column(name = "scanned_quantity")
    private Integer scannedQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scanned_by")
    private User scannedBy;

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;

    @PrePersist
    public void prePersist() {
        this.scannedAt = LocalDateTime.now();
    }
}