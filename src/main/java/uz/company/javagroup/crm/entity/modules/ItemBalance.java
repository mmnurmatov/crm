package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uz.isd.javagroup.grandcrm.entity.directories.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Table(name = "items_balance")
public class ItemBalance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_BALANCES_SEQUENCE")
    @SequenceGenerator(sequenceName = "ITEMS_BALANCES_SEQUENCE  ", allocationSize = 1, name = "ITEMS_BALANCES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "reg_date")
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wh_product_id")
    @JsonIgnore
    private WarehouseProduct warehouseProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    @JsonIgnore
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(name = "sale_id")
    Long saleId;

    @Column(name = "count", precision = 20, scale = 3)
    BigDecimal count;

    @Column(name = "remaining", precision = 20, scale = 3)
    BigDecimal remaining;

    @Column(name = "income_price", precision = 20, scale = 3)
    BigDecimal incomePrice = new BigDecimal(0);

    @Column(name = "price", precision = 20, scale = 3)
    BigDecimal price = new BigDecimal(0);

    @Column(name = "price_wholesale", precision = 20, scale = 3)
    BigDecimal priceWholesale = new BigDecimal(0);

    @Column(name = "amount", precision = 20, scale = 3)
    BigDecimal amount = new BigDecimal(0);

    @Column(name = "amount_wholesale", precision = 20, scale = 3)
    BigDecimal amountWholesale = new BigDecimal(0);
}
