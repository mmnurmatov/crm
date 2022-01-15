package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.ConversionItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.ConversionItemRepository;
import uz.isd.javagroup.grandcrm.services.modules.ConversionItemService;

import java.util.List;
import java.util.Optional;

@Service
public class ConversionItemServiceImpl implements ConversionItemService {

    @Autowired
    ConversionItemRepository conversionItemRepository;

    @Override
    public List<ConversionItem> findAll() {
        return conversionItemRepository.findAll();
    }

    @Override
    public void saveConversionItem(ConversionItem conversionItem) {
        conversionItemRepository.save(conversionItem);
    }

    @Override
    public ConversionItem getConversionItemById(Long id) throws RecordNotFoundException {
        Optional<ConversionItem> conversionItemOpt = conversionItemRepository.findById(id);
        if (conversionItemOpt.isPresent()) return conversionItemOpt.get();
        else throw new RecordNotFoundException("No ConversionItem record exists for id: " + id);
    }

    @Override
    public void deleteConversionItem(Long id) {
        conversionItemRepository.deleteById(id);
    }

    @Override
    public List<ConversionItem> findAllByConversionId(Long conversionId) {
        return conversionItemRepository.findAllByConversionId(conversionId);
    }

    @Override
    public List<Object[]> allByProductionIdByProductIdBySearchDate(Long productionId, int monthValue, int year) {
        return conversionItemRepository.allByProductionIdByProductIdBySearchDate(productionId, monthValue, year);
    }
}
