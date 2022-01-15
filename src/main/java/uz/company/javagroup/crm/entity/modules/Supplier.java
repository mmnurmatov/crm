package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLIERS_SEQUENCE")
    @SequenceGenerator(sequenceName = "SUPPLIERS_SEQUENCE", allocationSize = 1, name = "SUPPLIERS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    @Column(name = "status")
    private int status;

    @Column(name = "debet", precision = 20, scale = 3)
    private BigDecimal debet;


    @Column(name = "kredit", precision = 20, scale = 3)
    private BigDecimal kredit;

    @Column(name = "saldo", precision = 20, scale = 3)
    private BigDecimal saldo;

    @Column(name = "last_update")
    private Date lastUpdate;

}
