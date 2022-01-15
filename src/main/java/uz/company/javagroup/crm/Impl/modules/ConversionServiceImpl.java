package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Conversion;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.ConversionRepository;
import uz.isd.javagroup.grandcrm.services.modules.ConversionService;

import java.util.List;
import java.util.Optional;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Autowired
    ConversionRepository conversionRepository;

    @Override
    public List<Conversion> findAll() {
        return conversionRepository.findAll();
    }

    @Override
    public void saveConversion(Conversion conversion) {
        conversionRepository.save(conversion);
    }

    @Override
    public Conversion getConversionById(Long id) throws RecordNotFoundException {
        Optional<Conversion> conversionOpt = conversionRepository.findById(id);
        if (conversionOpt.isPresent()) return conversionOpt.get();
        else throw new RecordNotFoundException("No Conversion record exists for id: " + id);
    }

    @Override
    public void deleteConversion(Long id) {
        conversionRepository.deleteById(id);
    }

    @Override
    public List<Object[]> allByProductionIdByProductIdBySearchDate(Long productionId, Long productId, int monthValue, int year) {
        return conversionRepository.allByProductionIdByProductIdBySearchDate(productionId, productId, monthValue, year);
    }
}
