package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ProductUnit;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.ProductUnitRepository;
import uz.isd.javagroup.grandcrm.services.directories.ProductUnitService;

import java.util.List;
import java.util.Optional;

/**
 * Created by hp-pc on 5/12/2020.
 */
@Service
public class ProductUnitServiceImpl implements ProductUnitService {

    @Autowired
    private ProductUnitRepository productUnitRepository;

    @Override
    public List<ProductUnit> findAll() {
        List<ProductUnit> units = (List<ProductUnit>) productUnitRepository.findAll();
        return units;
    }

    @Override
    public void saveProductUnit(ProductUnit productUnit) {
        productUnitRepository.save(productUnit);
    }

    @Override
    public ProductUnit getProductUnitById(Long id) throws RecordNotFoundException {
        Optional<ProductUnit> productUnitOpt = productUnitRepository.findById(id);
        if (productUnitOpt.isPresent()) return productUnitOpt.get();
        else throw new RecordNotFoundException("No ProductUnit record exists for id: " + id);
    }

    @Override
    public void deleteProductUnit(Long id) {
        productUnitRepository.deleteById(id);
    }
}
