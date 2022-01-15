package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.TransactionTypeRepository;
import uz.isd.javagroup.grandcrm.services.directories.TransactionTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionTypeServiceImpl implements TransactionTypeService {

    @Autowired
    TransactionTypeRepository transactionTypeRepository;

    @Override
    public List<TransactionType> findAll() {
        return (List<TransactionType>) transactionTypeRepository.findAll();
    }

    @Override
    public void saveTransactionType(TransactionType transactionType) {
        transactionTypeRepository.save(transactionType);
    }

    @Override
    public TransactionType getTransactionTypeById(Long id) throws RecordNotFoundException {
        Optional<TransactionType> transactionTypeOpt = transactionTypeRepository.findById(id);
        if (transactionTypeOpt.isPresent()) return transactionTypeOpt.get();
        else throw new RecordNotFoundException("No Transaction Type record exists for id: " + id);
    }

    @Override
    public void deleteTransactionType(Long id) {
        transactionTypeRepository.deleteById(id);
    }
}
