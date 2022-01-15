package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.ItemStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemStatusRepository extends CrudRepository<ItemStatus, Long> {
    Optional<ItemStatus> findFirstByProductIdAndWarehouseIdAndWarehouseProductId(Long itemId, Long depId, Long docId);

    Optional<ItemStatus> findFirstByProductIdAndWarehouseIdAndPrice(Long itemId, Long depId, BigDecimal price);

    Optional<ItemStatus> findFirstByProductIdAndWarehouseIdAndIncomePrice(Long itemId, Long depId, BigDecimal price);

    Optional<ItemStatus> findFirstByProductIdAndWarehouseIdAndIncomePriceAndRemainingGreaterThan(Long itemId, Long depId, BigDecimal price, BigDecimal aZero);

    @Query(value = "SELECT i FROM ItemStatus i WHERE i.product.id = ?1 AND i.warehouse.id = ?2 AND i.remaining > 0 order by i.id")
    ItemStatus findFirstByProductIdAndWarehouseIdAndRemainingGreaterThan(Long productId, Long warehouseId, PageRequest of);

    Optional<ItemStatus> findFirstByProductIdAndWarehouseId(Long itemId, Long depId);

    Optional<ItemStatus> findFirstByProductIdAndWarehouseIdAndPriceWholesale(Long itemId, Long depId, BigDecimal price);

    @Query("SELECT i.product.id, i.product.nameUz, i.product.nameRu, i.product.nameEn, i.product.productCategory.article, i.product.productUnit.nameUz, SUM(i.remaining), i.incomePrice, i.price, i.priceWholesale FROM ItemStatus i WHERE i.warehouse.id = ?1 GROUP BY i.product.id, i.product.nameUz, i.product.nameRu, i.product.nameEn, i.product.productCategory.article, i.product.productUnit.nameUz, i.incomePrice, i.price, i.priceWholesale HAVING SUM(i.remaining) > 0")
    List<Object[]> warehouseBalance(Long warehouseId);

    @Query("SELECT i.product.id, i.product.nameUz, i.product.nameRu, i.product.nameEn, sum(i.remaining), i.incomePrice, i.product.productUnit.nameUz, i.product.productUnit.nameRu, i.product.productUnit.nameEn  FROM ItemStatus i " +
            "WHERE i.warehouse.id = ?1 AND i.remaining > 0 GROUP BY i.product.id, i.product.nameUz, i.product.nameRu, i.product.nameEn, i.incomePrice, i.product.productUnit.nameUz, i.product.productUnit.nameRu, i.product.productUnit.nameEn HAVING SUM(i.remaining) > 0 order by i.product.nameUz, i.product.nameRu, i.product.nameEn")
    List<Object[]> allByWarehouse(Long warehouseId, PageRequest of);

    @Query("SELECT i.product.id, i.product.nameUz, i.product.nameRu, i.product.nameEn, sum(i.remaining), i.incomePrice, i.product.productUnit.nameUz, i.product.productUnit.nameRu, i.product.productUnit.nameEn FROM ItemStatus i " +
            "WHERE i.warehouse.id = ?1 AND i.remaining > 0 AND (LOWER(i.product.nameUz) LIKE %?2% OR LOWER(i.product.nameRu) LIKE %?2% OR LOWER(i.product.nameEn) LIKE %?2%) " +
            "GROUP BY i.product.id, i.product.nameUz, i.product.nameRu, i.product.nameEn, i.incomePrice, i.product.productUnit.nameUz, i.product.productUnit.nameRu, i.product.productUnit.nameEn HAVING SUM(i.remaining) > 0 order by i.product.nameUz, i.product.nameRu, i.product.nameEn")
    List<Object[]> allByColumnByWarehouse(Long warehouseId, String keyword, PageRequest of);
}