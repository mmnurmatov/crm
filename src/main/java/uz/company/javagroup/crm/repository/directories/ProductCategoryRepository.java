package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.ProductCategory;

/**
 * Created by hp-pc on 5/12/2020.
 */
@Repository
public interface ProductCategoryRepository extends CrudRepository<ProductCategory, Long> {
}
