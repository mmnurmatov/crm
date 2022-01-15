package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import uz.isd.javagroup.grandcrm.entity.directories.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "warehouse_request_items")
public class WarehouseRequestItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_REQUESTS_ITEMS_SEQUENCE")
    @SequenceGenerator(sequenceName = "WAREHOUSE_REQUESTS_ITEMS_SEQUENCE", allocationSize = 1, name = "WAREHOUSE_REQUESTS_ITEMS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_request_id")
    @JsonIgnore
    private WarehouseRequest warehouseRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @Column(name = "count")
    private BigDecimal count;

    @Column(name = "remaining")
    private BigDecimal remaining;

    @Transient
    private BigDecimal sendCount1;
}
