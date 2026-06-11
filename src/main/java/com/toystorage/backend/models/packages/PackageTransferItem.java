package com.toystorage.backend.models.packages;

import com.toystorage.backend.models.products.Products;
import com.toystorage.backend.models.transfers.StockTransfer;
import com.toystorage.backend.models.transfers.StockTransferItems;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "package_transfer_items")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PackageTransferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Thùng hàng
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "package_id")
    private PackageBox packageBox;

    /*
     * Phiếu điều kho
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "transfer_id")
    private StockTransfer transfer;

    /*
     * Item điều kho
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "transfer_item_id")
    private StockTransferItems transferItem;

    /*
     * Sản phẩm
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "product_id")
    private Products product;

    /*
     * Số lượng
     */
    private Integer quantity = 0;
}