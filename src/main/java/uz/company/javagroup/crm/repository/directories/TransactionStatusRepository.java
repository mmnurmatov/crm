package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionStatus;

@Repository
public interface TransactionStatusRepository extends CrudRepository<TransactionStatus, Long> {
}
