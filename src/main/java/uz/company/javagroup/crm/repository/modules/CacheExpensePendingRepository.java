package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.CacheExpensePending;

@Repository
public interface CacheExpensePendingRepository extends JpaRepository<CacheExpensePending, Long> {
}
