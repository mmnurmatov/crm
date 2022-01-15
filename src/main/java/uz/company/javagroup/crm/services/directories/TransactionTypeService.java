package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface TransactionTypeService {

    List<TransactionType> findAll();

    void saveTransactionType(TransactionType transactionType);

    TransactionType getTransactionTypeById(Long id) throws RecordNotFoundException;

    void deleteTransactionType(Long id);

}
