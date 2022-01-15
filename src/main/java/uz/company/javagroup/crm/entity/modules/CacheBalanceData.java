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
@Table(name = "cache_balance_data")
public class CacheBalanceData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CACHEBALANCE_DATA_SEQUENCE")
    @SequenceGenerator(sequenceName = "CACHEBALANCE_DATA_SEQUENCE", allocationSize = 1, name = "CACHEBALANCE_DATA_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cache_id")
    @JsonIgnore
    Cache cache;

    @Column(name = "money_in_cache")
    private BigDecimal moneyInCache;

    @Column(name = "money_in_card")
    private BigDecimal moneyInCard;

    @Column(name = "money_in_terminal")
    private BigDecimal moneyInTerminal;

    @Column(name = "money_in_enumeration")
    private BigDecimal moneyInEnumeration;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
