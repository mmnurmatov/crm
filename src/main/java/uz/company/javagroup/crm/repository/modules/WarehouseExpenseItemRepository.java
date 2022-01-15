package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseExpenseItem;

import java.util.List;

@Repository
public interface WarehouseExpenseItemRepository extends CrudRepository<WarehouseExpenseItem, Long> {
    List<WarehouseExpenseItem> findAllByWarehouseExpenseId(Long warehouseExpenseId);
}
