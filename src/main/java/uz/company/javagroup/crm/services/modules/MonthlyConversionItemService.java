package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.MonthlyConversionItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface MonthlyConversionItemService {

    List<MonthlyConversionItem> findAll();

    void saveMonthlyConversionItem(MonthlyConversionItem monthlyConversionItem);

    MonthlyConversionItem getMonthlyConversionItemById(Long id) throws RecordNotFoundException;

    void deleteMonthlyConversionItem(Long id);
}
