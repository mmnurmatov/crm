package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "warehouse_requests")
public class WarehouseRequest implements Serializable {
    public enum DocumentStatus {
        DRAFT, WAITING, READY, PENDING, DELETED, CANCELLED, SUSPENDED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WAREHOUSE_REQUESTS_SEQUENCE")
    @SequenceGenerator(sequenceName = "WAREHOUSE_REQUESTS_SEQUENCE", allocationSize = 1, name = "WAREHOUSE_REQUESTS_SEQUENCE")
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

    @Column(name = "reg_date")
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    Date regDate;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "full_sent")
    private Boolean fullSent = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status")
    DocumentStatus documentStatus = DocumentStatus.DRAFT;

    @Transient
    List<WarehouseRequestItem> whItems;
}
