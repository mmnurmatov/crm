package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.ConversionItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface ConversionItemService {

    List<ConversionItem> findAll();

    void saveConversionItem(ConversionItem conversionItem);

    ConversionItem getConversionItemById(Long id) throws RecordNotFoundException;

    void deleteConversionItem(Long id);

    List<ConversionItem> findAllByConversionId(Long conversionId);

    List<Object[]> allByProductionIdByProductIdBySearchDate(Long productionId, int monthValue, int year);
}
