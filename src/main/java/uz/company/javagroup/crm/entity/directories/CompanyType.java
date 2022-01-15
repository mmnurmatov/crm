package uz.isd.javagroup.grandcrm.entity.directories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company_types")
public class CompanyType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_TYPES_SEQUENCE")
    @SequenceGenerator(sequenceName = "COMPANY_TYPES_SEQUENCE", allocationSize = 1, name = "COMPANY_TYPES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;
}
