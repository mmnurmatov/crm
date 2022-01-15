package uz.isd.javagroup.grandcrm.repository.directories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionType;

@Repository
public interface TransactionTypeRepository extends CrudRepository<TransactionType, Long> {
}
