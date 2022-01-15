package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.MonthlyConversion;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface MonthlyConversionService {

    List<MonthlyConversion> findAll();

    void saveMonthlyConversion(MonthlyConversion monthlyConversion);

    MonthlyConversion getMonthlyConversionById(Long id) throws RecordNotFoundException;

    void deleteMonthlyConversion(Long id);
}
