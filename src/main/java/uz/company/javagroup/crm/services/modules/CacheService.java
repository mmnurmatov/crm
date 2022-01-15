package uz.isd.javagroup.grandcrm.services.modules;

import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface CacheService {

    List<Cache> findAll();

    void saveCache(Cache cache);

    Cache getCacheById(Long id) throws RecordNotFoundException;

    void deleteCache(Long id);

    List<Cache> getAll(Long userId);
}
