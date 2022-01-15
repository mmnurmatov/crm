package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.ConversionItem;

import java.util.List;

@Repository
public interface ConversionItemRepository extends JpaRepository<ConversionItem, Long> {
    List<ConversionItem> findAllByConversionId(Long conversionId);

    @Query("SELECT c.product.id, c.product.nameUz, c.product.nameRu, c.product.nameEn, c.product.productUnit.nameUz, c.product.productUnit.nameRu, " +
            "c.product.productUnit.nameEn, SUM(c.count), SUM(c.price), SUM(c.amount) FROM ConversionItem c WHERE c.conversion.production.id = ?1 AND month(c.conversion.date) = ?2 AND year(c.conversion.date) = ?3 " +
            "GROUP BY c.product.id, c.product.nameUz, c.product.nameRu, c.product.nameEn, c.product.productUnit.nameUz, c.product.productUnit.nameRu, c.product.productUnit.nameEn")
    List<Object[]> allByProductionIdByProductIdBySearchDate(Long productionId, int monthValue, int year);
}
