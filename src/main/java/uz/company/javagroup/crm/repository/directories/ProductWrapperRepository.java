package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.ProductWrapper;

import java.util.List;

@Repository
public interface ProductWrapperRepository extends CrudRepository<ProductWrapper, Long> {
    List<ProductWrapper> findByParentId(Long parentId);
}
