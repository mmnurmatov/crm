package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ProductWrapper;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface ProductWrapperService {

    List<ProductWrapper> findAll();

    void saveProductWrapper(ProductWrapper productWrapper);

    ProductWrapper getProductWrapperById(Long id) throws RecordNotFoundException;

    void deleteProductWrapper(Long id);

    List<ProductWrapper> findByParent(Long parentId);
}
