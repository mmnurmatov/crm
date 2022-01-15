package uz.isd.javagroup.grandcrm.entity.directories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private Long productCategoryId;
    private String productCategoryUz;
    private String productCategoryRu;
    private String productCategoryEn;
    private Long productTypeId;
    private String productTypeNameUz;
    private String productTypeNameRu;
    private String productTypeNameEn;
    private Long productUnitId;
    private String productUnitNameUz;
    private String productUnitNameRu;
    private String productUnitNameEn;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private String photoName;
    private String modifiedPhotoName;
    private String photoExtension;
    private Boolean isWrapped = false;

    public ProductDto(Product product) {
        if (product.getId() != null) setId(product.getId());
        if (product.getProductCategory() != null && product.getProductCategory().getId() != null) {
            setProductCategoryId(product.getProductCategory().getId());
            setProductCategoryUz(product.getProductCategory().getNameUz());
            setProductCategoryRu(product.getProductCategory().getNameRu());
            setProductCategoryEn(product.getProductCategory().getNameEn());
        }
        if (product.getProductType() != null && product.getProductType().getId() != null) {
            setProductTypeId(product.getProductType().getId());
            setProductTypeNameUz(product.getProductType().getNameUz());
            setProductTypeNameRu(product.getProductType().getNameRu());
            setProductTypeNameEn(product.getProductType().getNameEn());
        }
        if (product.getProductUnit() != null && product.getProductUnit().getId() != null) {
            setProductUnitId(product.getProductUnit().getId());
            setProductUnitNameUz(product.getProductUnit().getNameUz());
            setProductUnitNameRu(product.getProductUnit().getNameRu());
            setProductUnitNameEn(product.getProductUnit().getNameEn());
        }
        setNameUz(product.getNameUz());
        setNameRu(product.getNameRu());
        setNameEn(product.getNameEn());
        setDescription(product.getDescription());
        setCreatedAt(product.getCreatedAt());
        setUpdatedAt(product.getUpdatedAt());
        setPhotoName(product.getPhotoName());
        setModifiedPhotoName(product.getModifiedPhotoName());
        setPhotoExtension(product.getPhotoExtension());
        setIsWrapped(product.getIsWrapped());
    }
}
