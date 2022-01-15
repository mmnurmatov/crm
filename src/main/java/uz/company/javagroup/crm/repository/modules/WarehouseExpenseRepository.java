package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseExpense;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;

import java.util.List;

@Repository
public interface WarehouseExpenseRepository extends CrudRepository<WarehouseExpense, Long> {
}
