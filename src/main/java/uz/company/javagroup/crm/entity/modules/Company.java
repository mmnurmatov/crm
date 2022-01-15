package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.directories.CompanyStatus;
import uz.isd.javagroup.grandcrm.entity.directories.CompanyType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
public class Company implements Serializable {
    public enum SaleType {
        RETAIL, WHOLESALE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANIES_SEQUENCE")
    @SequenceGenerator(sequenceName = "COMPANIES_SEQUENCE", allocationSize = 1, name = "COMPANIES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type")
    SaleType saleType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    Company parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_status_id")
    @JsonIgnore
    CompanyStatus companyStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_type_id")
    @JsonIgnore
    CompanyType companyType;
}
