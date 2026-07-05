package com.toystorage.backend.models.receipts;

import com.toystorage.backend.models.products.Products;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goods_receipt_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Phiếu nhập hàng
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_receipt_id")
    private GoodsReceipt goodsReceipt;

    /*
     * Sản phẩm trong phiếu nhập
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products product;

    /*
     * Số lượng cần nhập
     */
    @Column(name = "expected_quantity")
    private Integer expectedQuantity = 0;

    /*
     * Số lượng đã kiểm nhận
     */
    @Column(name = "received_quantity")
    private Integer receivedQuantity = 0;

    /*
     * Số lượng đã đưa lên kệ
     */
    @Column(name = "putaway_quantity")
    private Integer putawayQuantity = 0;
}