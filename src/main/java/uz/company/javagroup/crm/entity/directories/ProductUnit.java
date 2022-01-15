package uz.isd.javagroup.grandcrm.entity.directories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by hp-pc on 5/12/2020.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_units")
public class ProductUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_UNITS_SEQUENCE")
    @SequenceGenerator(sequenceName = "PRODUCT_UNITS_SEQUENCE", allocationSize = 1, name = "PRODUCT_UNITS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "code")
    private String code;
}
