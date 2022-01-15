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
@Table(name = "warehouse_expenses")
public class WarehouseExpense implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_EXPENSES_SEQUENCE")
    @SequenceGenerator(sequenceName = "WAREHOUSE_EXPENSES_SEQUENCE", allocationSize = 1, name = "WAREHOUSE_EXPENSES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonIgnore
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contragent_id")
    @JsonIgnore
    private Contragent contragent;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "summa")
    private BigDecimal summa;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
