package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.Conversion;

import java.util.List;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {
    @Query("SELECT SUM(c.count), SUM(c.price), SUM(c.amount) FROM Conversion c WHERE c.production.id = ?1 AND c.product.id = ?2 AND month(c.date) = ?3 AND year(c.date) = ?4")
    List<Object[]> allByProductionIdByProductIdBySearchDate(Long productionId, Long productId, int monthValue, int year);
}
