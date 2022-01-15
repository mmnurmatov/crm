package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.MonthlyConversionItem;

import java.util.List;

@Repository
public interface MonthlyConversionItemRepository extends JpaRepository<MonthlyConversionItem, Long> {

    List<MonthlyConversionItem> findAllByMonthlyConversionId(Long id);

}
