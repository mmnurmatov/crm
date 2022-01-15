package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ProductUnit;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Created by hp-pc on 5/12/2020.
 */
@Service
public interface ProductUnitService {

    List<ProductUnit> findAll();

    void saveProductUnit(ProductUnit productUnit);

    ProductUnit getProductUnitById(Long id) throws RecordNotFoundException;

    void deleteProductUnit(Long id);
}
