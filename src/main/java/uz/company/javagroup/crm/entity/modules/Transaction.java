package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends BaseController {

    public enum TransactionStatus {
        SUCCESS, PENDING, CANCELLED, FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTIONS_SEQUENCE")
    @SequenceGenerator(sequenceName = "TRANSACTIONS_SEQUENCE", allocationSize = 1, name = "TRANSACTIONS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cash_id")
    @JsonIgnore
    Cache cache;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_type_id")
    @JsonIgnore
    TransactionType transactionType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "money_type_id")
    @JsonIgnore
    MoneyType moneyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    Transaction.TransactionStatus transactionStatus;

    @Column(name = "debet", precision = 20, scale = 3)
    private BigDecimal debet;

    @Column(name = "kredit", precision = 20, scale = 3)
    private BigDecimal kredit;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "description")
    private String description;

}
