package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WarehouseProductItemRepository extends CrudRepository<WarehouseProductItem, Long> {
    @Query("SELECT w, (SELECT SUM(i.remaining) FROM ItemStatus i WHERE i.warehouse.id = w.warehouseProduct.fromWarehouse.id AND i.product.id = w.product.id AND i.incomePrice = w.incomePrice " +
            "GROUP BY i.product.id, i.incomePrice) FROM WarehouseProductItem w WHERE w.warehouseProduct.id = ?1")
    List<Object[]> findAllByWarehouseProductIdWithRemaining(Long warehouseProductId);

    List<WarehouseProductItem> findAllByWarehouseProductId(Long warehouseProductId);

    List<WarehouseProductItem> findAllByWarehouseProductIdAndRemainingIsGreaterThan(Long warehouseProductId, BigDecimal zero);

    @Query("SELECT w.product.nameUz, w.product.nameRu, w.product.nameEn, w.product.productCategory.article, w.product.productUnit.nameUz, w.product.productUnit.nameRu, w.product.productUnit.nameEn, w.count, w.incomePrice  FROM WarehouseProductItem w WHERE w.warehouseProduct.id = ?1")
    List<Object[]> findAllByWarehouseProductIdForWorkingReport(Long warehouseProductId);
}
