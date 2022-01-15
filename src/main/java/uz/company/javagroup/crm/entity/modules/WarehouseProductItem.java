package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.directories.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse_product_items")
public class WarehouseProductItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_PRODUCT_ITEMS_SEQUENCE")
    @SequenceGenerator(sequenceName = "WAREHOUSE_PRODUCT_ITEMS_SEQUENCE", allocationSize = 1, name = "WAREHOUSE_PRODUCT_ITEMS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_product_id")
//    @JsonIgnore
    private WarehouseProduct warehouseProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id")
    @JsonIgnore
    private Sector sector;

    @Column(name = "count")
    private BigDecimal count;

    @Column(name = "remaining")
    private BigDecimal remaining;

    @Column(name = "income_price")
    private BigDecimal incomePrice;

    @Column(name = "price", precision = 20, scale = 3)
    private BigDecimal price;

    @Column(name = "price_wholesale", precision = 20, scale = 3)
    private BigDecimal priceWholesale;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "production_date")
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    Date productionDate;

    @Column(name = "expire_date")
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    Date expireDate;
}
