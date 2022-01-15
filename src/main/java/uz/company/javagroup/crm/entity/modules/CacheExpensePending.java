package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cache_expense_pending")
public class CacheExpensePending implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CACHE_EXPENSE_PENDING_SEQUENCE")
    @SequenceGenerator(sequenceName = "CACHE_EXPENSE_PENDING_SEQUENCE", allocationSize = 1, name = "CACHE_EXPENSE_PENDING_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_expense_id")
    @JsonIgnore
    WarehouseProduct warehouseProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    @Column(name = "remaining", precision = 20, scale = 3)
    private BigDecimal remaining;

    public enum ActionStatus {
        PENDING, CLOSED
    }
}
