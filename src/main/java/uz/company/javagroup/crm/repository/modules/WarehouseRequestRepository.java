package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest;

import java.util.List;

@Repository
public interface WarehouseRequestRepository extends CrudRepository<WarehouseRequest, Long> {
    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2) AND w.fromWarehouse.id IN(?3) ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatusFromWarehouse(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus waiting, List<Long> ownWarehouseIds);

   @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2) AND w.fromWarehouse.id IN(?3) AND w.fromWarehouse.isProduction is true ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatusFromWarehouseProd(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus waiting, List<Long> ownWarehouseIds);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2 OR w.documentStatus = ?3) AND w.fromWarehouse.id IN(?4) ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatus3FromWarehouse(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus waiting, WarehouseRequest.DocumentStatus cancel, List<Long> ownWarehouseIds);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2 OR w.documentStatus = ?3 OR w.documentStatus = ?4) AND w.toWarehouse.id IN(?5) ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatus4ToWarehouse(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus waiting, WarehouseRequest.DocumentStatus cancel, WarehouseRequest.DocumentStatus pending, List<Long> ownWarehouseIds);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2) AND w.toWarehouse.id IN(?3) AND w.toWarehouse.isProduction IS NOT true ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatus4ToWarehouse2Status(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus pending, List<Long> ownWarehouseIds);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2) AND w.toWarehouse.id IN(?3) AND w.toWarehouse.isProduction IS true ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatus4ToWarehouse2StatusProd(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus pending, List<Long> ownWarehouseIds);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2 OR w.documentStatus = ?3 OR w.documentStatus = ?4) AND w.fromWarehouse.id IN(?5) AND w.fromWarehouse.isProduction IS NOT true ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatus4FromWarehouse(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus waiting, WarehouseRequest.DocumentStatus cancel, WarehouseRequest.DocumentStatus pending, List<Long> ownWarehouseIds);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseRequestItem ic WHERE ic.warehouseRequest.id = w.id) " +
            "FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2 OR w.documentStatus = ?3 OR w.documentStatus = ?4) AND w.fromWarehouse.id IN(?5) AND w.fromWarehouse.isProduction IS true ORDER BY w.id DESC")
    List<Object[]> ownWithSumQtyAndStatus4FromWarehouseProd(WarehouseRequest.DocumentStatus ready, WarehouseRequest.DocumentStatus waiting, WarehouseRequest.DocumentStatus cancel, WarehouseRequest.DocumentStatus pending, List<Long> ownWarehouseIds);

    @Query("SELECT count(w) FROM WarehouseRequest w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2) AND w.fromWarehouse.id = ?3")
    Long byFromWarehouseIdAndStatus2(WarehouseRequest.DocumentStatus waiting, WarehouseRequest.DocumentStatus ready, Long warehouseId);
}
