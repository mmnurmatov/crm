package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.ProductionExpense;

import java.util.List;

@Repository
public interface ProductionExpenseRepository extends CrudRepository<ProductionExpense, Long> {

    List<ProductionExpense> findAllByMonthlyConversionId(Long id);

}
