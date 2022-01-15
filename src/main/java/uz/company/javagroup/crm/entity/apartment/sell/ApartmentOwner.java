package uz.isd.javagroup.grandcrm.entity.apartment.sell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "apartment_owners")
public class ApartmentOwner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APARTMENT_OWNER_SEQUENCE")
    @SequenceGenerator(sequenceName = "APARTMENT_OWNER_SEQUENCE", allocationSize = 1, name = "APARTMENT_OWNER_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "passport_data", unique = true, nullable = false)
    private String passportData;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
