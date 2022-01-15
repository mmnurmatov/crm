package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.ProductUnit;

/**
 * Created by hp-pc on 5/12/2020.
 */
@Repository
public interface ProductUnitRepository extends CrudRepository<ProductUnit, Long> {
}
