package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.TransactionStatusRepository;
import uz.isd.javagroup.grandcrm.services.directories.TransactionStatusService;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionStatusServiceImpl implements TransactionStatusService {

    @Autowired
    TransactionStatusRepository transactionStatusRepository;


    @Override
    public List<TransactionStatus> findAll() {
        return (List<TransactionStatus>) transactionStatusRepository.findAll();
    }

    @Override
    public void saveTransactionStatus(TransactionStatus transactionStatus) {
        transactionStatusRepository.save(transactionStatus);
    }

    @Override
    public TransactionStatus getTransactionStatusById(Long id) throws RecordNotFoundException {
        Optional<TransactionStatus> transactionStatusOpt = transactionStatusRepository.findById(id);
        if (transactionStatusOpt.isPresent()) return transactionStatusOpt.get();
        else throw new RecordNotFoundException("No Transaction Status record exists for id: " + id);
    }

    @Override
    public void deleteTransactionStatus(Long id) {
        transactionStatusRepository.deleteById(id);
    }
}
