package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ProductWrapper;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.ProductWrapperRepository;
import uz.isd.javagroup.grandcrm.services.directories.ProductWrapperService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductWrapperServiceImpl implements ProductWrapperService {

    @Autowired
    private ProductWrapperRepository productWrapperRepository;

    @Override
    public List<ProductWrapper> findAll() {
        return (List<ProductWrapper>) productWrapperRepository.findAll();
    }

    @Override
    public void saveProductWrapper(ProductWrapper productWrapper) {
        productWrapperRepository.save(productWrapper);
    }

    @Override
    public ProductWrapper getProductWrapperById(Long id) throws RecordNotFoundException {
        Optional<ProductWrapper> productWrapperOpt = productWrapperRepository.findById(id);
        if (productWrapperOpt.isPresent()) return productWrapperOpt.get();
        else throw new RecordNotFoundException("No ProductWrapper record exists for id: " + id);
    }

    @Override
    public void deleteProductWrapper(Long id) {
        productWrapperRepository.deleteById(id);
    }

    @Override
    public List<ProductWrapper> findByParent(Long parentId) {
        return productWrapperRepository.findByParentId(parentId);
    }
}
