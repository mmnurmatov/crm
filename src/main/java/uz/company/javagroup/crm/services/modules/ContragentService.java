package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Contragent;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface ContragentService {

    List<Contragent> findAll();

    void saveContragent(Contragent contragent);

    Contragent getContragentById(Long id) throws RecordNotFoundException;

    void deleteContragent(Long id);
}
