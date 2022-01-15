package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.CacheBalanceData;
import uz.isd.javagroup.grandcrm.repository.modules.CacheBalanceDataRepository;
import uz.isd.javagroup.grandcrm.services.modules.CacheBalanceDataService;

import java.util.List;
import java.util.Optional;

@Service
public class CacheBalanceDataServiceImpl implements CacheBalanceDataService {

    @Autowired
    CacheBalanceDataRepository cacheBalanceDataRepository;

    @Override
    public List<CacheBalanceData> findAll() {
        return cacheBalanceDataRepository.findAll();
    }

    @Override
    public Optional<CacheBalanceData> findByCacheId(Long cacheId) {
        return Optional.ofNullable(cacheBalanceDataRepository.findByCacheId(cacheId));
    }

    @Override
    public void saveCacheBalanceData(CacheBalanceData cacheBalanceData) {
        cacheBalanceDataRepository.save(cacheBalanceData);
    }

}
