package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.directories.ExpenseType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "production_expenses")
public class ProductionExpense implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTION_EXPENSES_SEQUENCE")
    @SequenceGenerator(sequenceName = "PRODUCTION_EXPENSES_SEQUENCE", allocationSize = 1, name = "PRODUCTION_EXPENSES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthly_conversion_id")
    @JsonIgnore
    private MonthlyConversion monthlyConversion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_type_id")
    @JsonIgnore
    private ExpenseType expenseType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "over_all")
    private BigDecimal overAll;
}
