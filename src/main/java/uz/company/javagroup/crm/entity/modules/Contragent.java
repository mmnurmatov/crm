package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import uz.isd.javagroup.grandcrm.entity.directories.Region;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "contragents")
@Getter
@Setter
public class Contragent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRAGENTS_SEQUENCE")
    @SequenceGenerator(sequenceName = "CONTRAGENTS_SEQUENCE", allocationSize = 1, name = "CONTRAGENTS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "full_name")
    @Size(min = 3, max = 64, message = "Full name size must be between 3 and 64")
    private String fullName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "phone", length = 32)
    @Size(max = 32, message = "Phone size must be between 0 and 32")
    private String phone;

    @Column(name = "mobile", length = 32)
    @Size(max = 32, message = "Mobile size must be between 0 and 32")
    private String mobile;

    @Column(name = "email", length = 32)
    @Size(max = 32, message = "Email size must be between 0 and 32")
    @Email
    private String email;

    @Column(name = "address")
    @Size(max = 255, message = "Address size must be between 0 and 255")
    private String address;

    @Column(name = "address2")
    @Size(max = 255, message = "Address2 size must be between 0 and 255")
    private String address2;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    @JsonIgnore
    private Region region;

    @Column(name = "INN")
    private String inn;

    @Column(name = "OKONX")
    private String okonx;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "vat_account")
    private String vatAccount;

//    @ManyToMany(cascade = {CascadeType.MERGE}, targetEntity = Department.class, fetch = FetchType.LAZY)
//    @JoinTable(name = "contragent_department", joinColumns = {@JoinColumn(name = "contragent_id")}, inverseJoinColumns = {@JoinColumn(name = "department_id")})
//    Set<Department> departments = new LinkedHashSet<>();

    /**
     * fiz litso, yur litso, postavshik, proizvoditel...
     */
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    Reference category;

    /**
     * VIP, Doimiy...
     */
//    @ManyToOne
//    @JoinColumn(name = "group_id")
//    Reference group;
}
