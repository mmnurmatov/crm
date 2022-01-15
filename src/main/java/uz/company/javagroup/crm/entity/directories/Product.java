package uz.isd.javagroup.grandcrm.entity.directories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTS_SEQUENCE")
    @SequenceGenerator(sequenceName = "PRODUCTS_SEQUENCE", allocationSize = 1, name = "PRODUCTS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_categories_id")
    @JsonIgnore
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_types_id")
    @JsonIgnore
    private ProductType productType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_unit_id")
    private ProductUnit productUnit;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "modified_photo_name")
    private String modifiedPhotoName;

    @Column(name = "photo_extension")
    private String photoExtension;

    @Column(name = "is_wrapped")
    private Boolean isWrapped = false;

    @Transient
    private MultipartFile productPhoto;
}
