package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uz.isd.javagroup.grandcrm.entity.directories.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Table(name = "items_statuses")
public class ItemStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_STATUSES_SEQUENCE")
    @SequenceGenerator(sequenceName = "ITEMS_STATUSES_SEQUENCE  ", allocationSize = 1, name = "ITEMS_STATUSES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonIgnore
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wh_product_id", nullable = false)
    @JsonIgnore
    private WarehouseProduct warehouseProduct;

    @Column(name = "income_price", precision = 20, scale = 3)
    private BigDecimal incomePrice;

    @Column(name = "price", precision = 20, scale = 3)
    private BigDecimal price;

    @Column(name = "price_wholesale", precision = 20, scale = 3)
    private BigDecimal priceWholesale;

    @Column(name = "remaining", precision = 20, scale = 3)
    private BigDecimal remaining;

    @Column(name = "sector_id")
    private Long sector;
}
