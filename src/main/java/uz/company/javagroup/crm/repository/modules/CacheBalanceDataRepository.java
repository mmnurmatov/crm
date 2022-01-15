package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.CacheBalanceData;


@Repository
public interface CacheBalanceDataRepository extends JpaRepository<CacheBalanceData, Long> {

    CacheBalanceData findByCacheId(Long cacheId);

}
