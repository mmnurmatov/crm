package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ExpenseType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.ExpenseTypeRepository;
import uz.isd.javagroup.grandcrm.services.directories.ExpenseTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseTypeServiceImpl implements ExpenseTypeService {

    @Autowired
    ExpenseTypeRepository expenseTypeRepository;

    @Override
    public List<ExpenseType> findAll() {
        return expenseTypeRepository.findAll();
    }
    @Override
    public List<ExpenseType> findAllByType(Integer type) {
        return expenseTypeRepository.findAllByType(type);
    }

    @Override
    public void saveExpenseType(ExpenseType expenseType) {
        expenseTypeRepository.save(expenseType);
    }

    @Override
    public ExpenseType getExpenseTypeById(Long id) throws RecordNotFoundException {
        Optional<ExpenseType> expenseTypeOpt = expenseTypeRepository.findById(id);
        if (expenseTypeOpt.isPresent()) return expenseTypeOpt.get();
        else throw new RecordNotFoundException("No Area record exists for id: " + id);
    }

    @Override
    public void deleteExpenseType(Long id) {
        expenseTypeRepository.deleteById(id);
    }
}
