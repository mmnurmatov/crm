package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "caches")
public class Cache implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CACHES_SEQUENCE")
    @SequenceGenerator(sequenceName = "CACHES_SEQUENCE", allocationSize = 1, name = "CACHES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    Company company;

    @Column(name = "status")
    private int status;

    @Column(name = "debet", precision = 20, scale = 3)
    private BigDecimal debet;

    @Column(name = "kredit", precision = 20, scale = 3)
    private BigDecimal kredit;

    @Column(name = "balance", precision = 20, scale = 3)
    private BigDecimal balance;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "last_transaction_time")
    private Timestamp lastTransactionTime;

}
