package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;

@Repository
public interface MoneyTypeRepository extends CrudRepository<MoneyType, Long> {
}
