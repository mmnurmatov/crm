package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.MonthlyConversionItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.MonthlyConversionItemRepository;
import uz.isd.javagroup.grandcrm.services.modules.MonthlyConversionItemService;

import java.util.List;
import java.util.Optional;

@Service
public class MonthlyConversionItemServiceImpl implements MonthlyConversionItemService {

    @Autowired
    MonthlyConversionItemRepository monthlyConversionItemRepository;

    @Override
    public List<MonthlyConversionItem> findAll() {
        return monthlyConversionItemRepository.findAll();
    }

    @Override
    public void saveMonthlyConversionItem(MonthlyConversionItem monthlyConversionItem) {
        monthlyConversionItemRepository.save(monthlyConversionItem);
    }

    @Override
    public MonthlyConversionItem getMonthlyConversionItemById(Long id) throws RecordNotFoundException {
        Optional<MonthlyConversionItem> monthlyConversionItemOpt = monthlyConversionItemRepository.findById(id);
        if (monthlyConversionItemOpt.isPresent()) return monthlyConversionItemOpt.get();
        else throw new RecordNotFoundException("No MonthlyConversionItem record exists for id: " + id);
    }

    @Override
    public void deleteMonthlyConversionItem(Long id) {
        monthlyConversionItemRepository.deleteById(id);
    }
}
