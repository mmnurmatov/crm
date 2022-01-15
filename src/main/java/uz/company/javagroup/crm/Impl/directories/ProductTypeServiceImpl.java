package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ProductType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.ProductTypeRepository;
import uz.isd.javagroup.grandcrm.services.directories.ProductTypeService;

import java.util.List;
import java.util.Optional;

/**
 * Created by hp-pc on 5/12/2020.
 */

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public List<ProductType> findAll() {
        List<ProductType> types = (List<ProductType>) productTypeRepository.findAll();
        return types;
    }

    @Override
    public void saveProductType(ProductType productType) {
        productTypeRepository.save(productType);
    }

    @Override
    public ProductType getProductTypeById(Long id) throws RecordNotFoundException {
        Optional<ProductType> typeOpt = productTypeRepository.findById(id);
        if (typeOpt.isPresent()) return typeOpt.get();
        else throw new RecordNotFoundException("No ProductType record exists for id: " + id);
    }

    @Override
    public void deleteProductType(Long id) {
        productTypeRepository.deleteById(id);
    }
}
