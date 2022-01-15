package uz.isd.javagroup.grandcrm.repository.modules;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.Cache;

import java.util.List;

@Repository
public interface CacheRepository extends CrudRepository<Cache, Long> {

    @Query(value = "select * from CACHES c left join CACHE_USERS CU on c.ID = CU.CACHE_ID where cu.USER_ID = ?1", nativeQuery = true)
    List<Cache> getAll(Long userId);

}
