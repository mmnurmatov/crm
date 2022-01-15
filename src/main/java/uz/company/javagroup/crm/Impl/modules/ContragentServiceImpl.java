package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Contragent;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.ContragentRepository;
import uz.isd.javagroup.grandcrm.services.modules.ContragentService;

import java.util.List;
import java.util.Optional;

@Service
public class ContragentServiceImpl implements ContragentService {

    @Autowired
    ContragentRepository contragentRepository;

    @Override
    public List<Contragent> findAll() {
        return contragentRepository.findAll();
    }

    @Override
    public void saveContragent(Contragent contragent) {
        contragentRepository.save(contragent);
    }

    @Override
    public Contragent getContragentById(Long id) throws RecordNotFoundException {
        Optional<Contragent> contragentOpt = contragentRepository.findById(id);
        if (contragentOpt.isPresent()) return contragentOpt.get();
        else throw new RecordNotFoundException("No Contragent record exists for id: " + id);
    }

    @Override
    public void deleteContragent(Long id) {
        contragentRepository.deleteById(id);
    }
}
