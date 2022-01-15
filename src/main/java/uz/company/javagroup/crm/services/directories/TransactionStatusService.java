package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface TransactionStatusService {

    List<TransactionStatus> findAll();

    void saveTransactionStatus(TransactionStatus transactionStatus);

    TransactionStatus getTransactionStatusById(Long id) throws RecordNotFoundException;

    void deleteTransactionStatus(Long id);

}
