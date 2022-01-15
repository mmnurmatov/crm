package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "warehouse_products")
public class WarehouseProduct implements Serializable {
    public enum Action {
        KIRIM, KOCHIRISH, QAYTARISH, SPISAT, CHIQIM
    }

    public enum DocumentStatus {
        DRAFT, WAITING, READY, PENDING, DELETED, CANCELLED, SUSPENDED
    }

    public enum DocumentType {
        NAKLADNOY, AKT, PROTOKOL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_PRODUCTS_SEQUENCE")
    @SequenceGenerator(sequenceName = "WAREHOUSE_PRODUCTS_SEQUENCE", allocationSize = 1, name = "WAREHOUSE_PRODUCTS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_warehouse_id")
    @JsonIgnore
    private Warehouse fromWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_warehouse_id")
    @JsonIgnore
    private Warehouse toWarehouse;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "reason")
    private String reason;

    @Column(name = "summa")
    private BigDecimal summa;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_warehouse_id")
    @JsonIgnore
    private WarehouseProduct linkedWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contragent_id")
    @JsonIgnore
    private Contragent contragent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inked_request_id")
    @JsonIgnore
    private WarehouseRequest linkedWarehouseRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    Action action;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status")
    DocumentStatus documentStatus = DocumentStatus.DRAFT;

    @Transient
    List<WarehouseProductItem> whItems;
}
