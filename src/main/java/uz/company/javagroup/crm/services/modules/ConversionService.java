package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Conversion;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface ConversionService {

    List<Conversion> findAll();

    void saveConversion(Conversion conversion);

    Conversion getConversionById(Long id) throws RecordNotFoundException;

    void deleteConversion(Long id);

    List<Object[]> allByProductionIdByProductIdBySearchDate(Long productionId, Long productId, int monthValue, int year);
}
