package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.CompanyType;

@Repository
public interface CompanyTypeRepository extends CrudRepository<CompanyType, Long> {
}
