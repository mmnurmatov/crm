package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ProductType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Created by hp-pc on 5/12/2020.
 */

@Service
public interface ProductTypeService {

    List<ProductType> findAll();

    void saveProductType(ProductType productType);

    ProductType getProductTypeById(Long id) throws RecordNotFoundException;

    void deleteProductType(Long id);
}
