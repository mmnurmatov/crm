package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse_expense_items")
public class WarehouseExpenseItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_EXPENSE_ITEMS_SEQUENCE")
    @SequenceGenerator(sequenceName = "WAREHOUSE_EXPENSE_ITEMS_SEQUENCE", allocationSize = 1, name = "WAREHOUSE_EXPENSE_ITEMS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_expense_id")
    @JsonIgnore
    private WarehouseExpense warehouseExpense;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_product_item_id")
    @JsonIgnore
    private WarehouseProductItem warehouseProductItem;

    @Column(name = "outgoing_count")
    private BigDecimal outgoingCount;

    @Column(name = "remaining")
    private BigDecimal remaining;

    @Column(name = "outgoing_price")
    private BigDecimal outgoingPrice;

    @Column(name = "summa")
    private BigDecimal summa;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
