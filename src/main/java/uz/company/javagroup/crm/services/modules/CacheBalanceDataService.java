package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.CacheBalanceData;

import java.util.List;
import java.util.Optional;

@Service
public interface CacheBalanceDataService {

    List<CacheBalanceData> findAll();

    Optional<CacheBalanceData> findByCacheId(Long cacheId);

    void saveCacheBalanceData(CacheBalanceData cacheBalanceData);

}
