package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.MonthlyConversion;

@Repository
public interface MonthlyConversionRepository extends JpaRepository<MonthlyConversion, Long> {
}
