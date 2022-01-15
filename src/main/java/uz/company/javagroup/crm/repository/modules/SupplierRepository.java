package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.Supplier;

@Repository
public interface SupplierRepository extends CrudRepository<Supplier, Long> {
}
