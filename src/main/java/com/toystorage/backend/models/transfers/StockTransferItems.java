package com.toystorage.backend.models.transfers;

import com.toystorage.backend.models.products.Products;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_transfer_items")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class StockTransferItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Phiếu điều kho
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "transfer_id")
    private StockTransfer transfer;

    /*
     * Sản phẩm
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "product_id")
    private Products product;

    /*
     * SL yêu cầu
     */
    @Column(name = "requested_quantity")
    private Integer requestedQuantity = 0;

    /*
     * SL đã pick
     */
    @Column(name = "picked_quantity")
    private Integer pickedQuantity = 0;

    /*
     * SL đã nhận
     */
    @Column(name = "received_quantity")
    private Integer receivedQuantity = 0;
}