package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequestItem;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WarehouseRequestItemRepository extends CrudRepository<WarehouseRequestItem, Long> {
    List<WarehouseRequestItem> findAllByWarehouseRequestId(Long warehouseRequestId);

    @Query("SELECT r, (SELECT SUM(i.remaining) FROM ItemStatus i WHERE i.product.id = r.product.id AND i.warehouse.id = r.warehouseRequest.toWarehouse.id) FROM WarehouseRequestItem r WHERE r.warehouseRequest.id = ?1 AND r.remaining > ?2 ORDER BY r.id")
    List<Object[]> findAllByWarehouseRequestIdAndRemainingGreaterThan(Long warehouseRequestId, BigDecimal remaining);

    @Query("SELECT r, (SELECT SUM(i.remaining) FROM ItemStatus i WHERE i.product.id = r.product.id AND i.warehouse.id = r.warehouseRequest.toWarehouse.id) FROM WarehouseRequestItem r WHERE r.warehouseRequest.id = ?1 ORDER BY r.id")
    List<Object[]> findAllByWarehouseRequestIdReadyStatus(Long warehouseRequestId);

    @Query("SELECT r FROM WarehouseRequestItem r WHERE r.warehouseRequest.id = ?1 AND r.remaining > ?2")
    List<WarehouseRequestItem> findAllByWarehouseRequestIdAndRemainingGreaterThanNative(Long warehouseRequestId, BigDecimal remaining);
}
