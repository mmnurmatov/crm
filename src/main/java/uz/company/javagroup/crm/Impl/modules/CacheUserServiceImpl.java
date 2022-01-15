package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.CacheUser;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.CacheUserRepository;
import uz.isd.javagroup.grandcrm.services.modules.CacheUserService;

import java.util.List;
import java.util.Optional;

@Service
public class CacheUserServiceImpl implements CacheUserService {

    @Autowired
    CacheUserRepository cacheUserRepository;

    @Override
    public List<CacheUser> findAll() {
        return (List<CacheUser>) cacheUserRepository.findAll();
    }

    @Override
    public void saveCacheUser(CacheUser cacheUser) {
        cacheUserRepository.save(cacheUser);
    }

    @Override
    public CacheUser getCacheUserById(Long id) throws RecordNotFoundException {
        Optional<CacheUser> cacheUserOptional = cacheUserRepository.findById(id);
        if (cacheUserOptional.isPresent()) return cacheUserOptional.get();
        else throw new RecordNotFoundException("No Cache User record exists for id: " + id);
    }

    @Override
    public void deleteCacheUser(Long id) {
        cacheUserRepository.deleteById(id);
    }

    @Override
    public List<CacheUser> findAllByCacheId(Long cacheId) {
        return cacheUserRepository.findAllByCacheId(cacheId);
    }
}
