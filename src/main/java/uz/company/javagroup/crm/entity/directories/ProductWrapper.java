package uz.isd.javagroup.grandcrm.entity.directories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_wrappers")
public class ProductWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_WRAPPER_SEQUENCE")
    @SequenceGenerator(sequenceName = "PRODUCT_WRAPPER_SEQUENCE", allocationSize = 1, name = "PRODUCT_WRAPPER_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Product parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wrapped_id")
    @JsonIgnore
    private Product wrapped;

    @Column(name = "amount")
    private BigDecimal amount;
}
