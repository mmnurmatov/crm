package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface MoneyTypeService {

    List<MoneyType> findAll();

    void saveMoneyType(MoneyType moneyType);

    MoneyType getMoneyTypeById(Long id) throws RecordNotFoundException;

    void deleteMoneyType(Long id);

}
