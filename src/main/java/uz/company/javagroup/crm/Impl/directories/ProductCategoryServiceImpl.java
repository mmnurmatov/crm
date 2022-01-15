package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ProductCategory;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.ProductCategoryRepository;
import uz.isd.javagroup.grandcrm.services.directories.ProductCategoryService;

import java.util.List;
import java.util.Optional;

/**
 * Created by hp-pc on 5/12/2020.
 */

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> findAll() {
        List<ProductCategory> categories = (List<ProductCategory>) productCategoryRepository.findAll();
        return categories;
    }

    @Override
    public void saveProductCategory(ProductCategory productCategory) {
        productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory getProductCategoryById(Long id) throws RecordNotFoundException {
        Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) return categoryOpt.get();
        else throw new RecordNotFoundException("No ProductCategory record exists for id: " + id);
    }

    @Override
    public void deleteProductCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}
