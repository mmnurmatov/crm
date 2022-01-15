package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.CacheUser;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface CacheUserService {

    List<CacheUser> findAll();

    void saveCacheUser(CacheUser cacheUser);

    CacheUser getCacheUserById(Long id) throws RecordNotFoundException;

    void deleteCacheUser(Long id);

    List<CacheUser> findAllByCacheId(Long cacheId);

}
