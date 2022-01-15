package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseExpense;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface WarehouseExpenseService {

    List<WarehouseExpense> findAll();

    void saveWarehouseExpense(WarehouseExpense warehouseExpense);

    WarehouseExpense getWarehouseExpenseById(Long id) throws RecordNotFoundException;

    void deleteWarehouseExpense(Long id);
}
