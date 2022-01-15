package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.MonthlyConversion;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.MonthlyConversionRepository;
import uz.isd.javagroup.grandcrm.services.modules.MonthlyConversionService;

import java.util.List;
import java.util.Optional;

@Service
public class MonthlyConversionServiceImpl implements MonthlyConversionService {

    @Autowired
    MonthlyConversionRepository monthlyConversionRepository;

    @Override
    public List<MonthlyConversion> findAll() {
        return monthlyConversionRepository.findAll();
    }

    @Override
    public void saveMonthlyConversion(MonthlyConversion monthlyConversion) {
        monthlyConversionRepository.save(monthlyConversion);
    }

    @Override
    public MonthlyConversion getMonthlyConversionById(Long id) throws RecordNotFoundException {
        Optional<MonthlyConversion> monthlyConversionOpt = monthlyConversionRepository.findById(id);
        if (monthlyConversionOpt.isPresent()) return monthlyConversionOpt.get();
        else throw new RecordNotFoundException("No MonthlyConversion record exists for id: " + id);
    }

    @Override
    public void deleteMonthlyConversion(Long id) {
        monthlyConversionRepository.deleteById(id);
    }
}
