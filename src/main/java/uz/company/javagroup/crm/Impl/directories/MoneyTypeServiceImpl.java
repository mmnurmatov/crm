package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.MoneyTypeRepository;
import uz.isd.javagroup.grandcrm.services.directories.MoneyTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class MoneyTypeServiceImpl implements MoneyTypeService {

    @Autowired
    MoneyTypeRepository moneyTypeRepository;

    @Override
    public List<MoneyType> findAll() {
        return (List<MoneyType>) moneyTypeRepository.findAll();
    }

    @Override
    public void saveMoneyType(MoneyType moneyType) {
        moneyTypeRepository.save(moneyType);
    }

    @Override
    public MoneyType getMoneyTypeById(Long id) throws RecordNotFoundException {
        Optional<MoneyType> moneyTypeOpt = moneyTypeRepository.findById(id);
        if (moneyTypeOpt.isPresent()) return moneyTypeOpt.get();
        else throw new RecordNotFoundException("No Money Type record exists for id: " + id);
    }

    @Override
    public void deleteMoneyType(Long id) {
        moneyTypeRepository.deleteById(id);
    }
}
