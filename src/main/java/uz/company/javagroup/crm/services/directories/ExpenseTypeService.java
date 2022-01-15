package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.ExpenseType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface ExpenseTypeService {

    List<ExpenseType> findAll();

    void saveExpenseType(ExpenseType expenseType);

    ExpenseType getExpenseTypeById(Long id) throws RecordNotFoundException;

    void deleteExpenseType(Long id);

    List<ExpenseType> findAllByType(Integer type);
}
