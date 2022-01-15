package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.entity.directories.ProductCategory;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

/**
 * Created by hp-pc on 5/12/2020.
 */
@Service
public interface ProductCategoryService {

    List<ProductCategory> findAll();

    void saveProductCategory(ProductCategory productCategory);

    ProductCategory getProductCategoryById(Long id) throws RecordNotFoundException;

    void deleteProductCategory(Long id);
}
