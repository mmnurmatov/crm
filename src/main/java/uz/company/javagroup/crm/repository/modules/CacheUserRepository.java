package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.CacheUser;

import java.util.List;

@Repository
public interface CacheUserRepository extends CrudRepository<CacheUser, Long> {

    List<CacheUser> findAllByCacheId(Long cacheId);

}
