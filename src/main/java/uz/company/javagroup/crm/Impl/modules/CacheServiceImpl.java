package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.CacheRepository;
import uz.isd.javagroup.grandcrm.services.modules.CacheService;

import java.util.List;
import java.util.Optional;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    CacheRepository cacheRepository;

    @Override
    public List<Cache> findAll() {
        List<Cache> cacheList = (List<Cache>) cacheRepository.findAll();
        return cacheList;
    }

    @Override
    public void saveCache(Cache cache) {
        cacheRepository.save(cache);
    }

    @Override
    public Cache getCacheById(Long id) throws RecordNotFoundException {
        Optional<Cache> cashOptional = cacheRepository.findById(id);
        if (cashOptional.isPresent()) return cashOptional.get();
        else throw new RecordNotFoundException("No Cache record exists for id: " + id);
    }

    @Override
    public void deleteCache(Long id) {
        cacheRepository.deleteById(id);
    }

    @Override
    public List<Cache> getAll(Long userId) {
        return cacheRepository.getAll(userId);
    }
}
