package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseExpenseItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface WarehouseExpenseItemService {

    List<WarehouseExpenseItem> findAll();

    void saveWarehouseExpenseItem(WarehouseExpenseItem warehouseExpenseItem);

    WarehouseExpenseItem getWarehouseExpenseItemById(Long id) throws RecordNotFoundException;

    void deleteWarehouseExpenseItem(Long id);

    List<WarehouseExpenseItem> findAllByWarehouseExpenseId(Long aLong);
}
