package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseProductRepository extends CrudRepository<WarehouseProduct, Long> {

    Optional<WarehouseProduct> findWarehouseProductById(String warehouseProductId);

    Optional<WarehouseProduct> findWarehouseProductByRegNumber(String regNumber);

    @Query("SELECT count(w) FROM WarehouseProduct w WHERE w.toWarehouse.id = ?1 AND w.action = ?2")
    Long findByToWarehouseIdAndAction(Long toWarehouseId, WarehouseProduct.Action action);

    @Query("SELECT count(w) FROM WarehouseProduct w WHERE w.fromWarehouse.id = ?1 AND w.action = ?2")
    Long findByFromWarehouseIdAndAction(Long fromWarehouseId, WarehouseProduct.Action action);

    //    @Query("SELECT w, SUM(wp.count), SUM(wp.count * wp.incomePrice) FROM WarehouseProduct w LEFT JOIN WarehouseProductItem wp ON w.id = wp.warehouseProduct.id GROUP BY w")
    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), (SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) FROM WarehouseProduct w")
    List<Object[]> allWithSummaAndQty();

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), (SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) FROM WarehouseProduct w WHERE w.action = ?1 AND w.linkedWarehouse is null ORDER BY w.id DESC")
    List<Object[]> allWithSummaAndQtyAndAction(WarehouseProduct.Action action);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.action = ?1 AND w.linkedWarehouse is null AND w.toWarehouse.id IN(?2) ORDER BY w.id DESC")
    List<Object[]> ownWithSummaAndQtyAndActionToWarehouse(WarehouseProduct.Action kochirish, List<Long> ownWarehouses);

    @Query("SELECT w, w.toWarehouse.name, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.action = ?1 AND w.linkedWarehouse is null AND w.toWarehouse.id =?2 AND day(w.createdAt) = ?3  AND month(w.createdAt) = ?4 AND year(w.createdAt) = ?5  ORDER BY w.createdAt")
    List<Object[]> getAllWarehouseForWorkingReportByToWarehouse(WarehouseProduct.Action kochirish, Long warehouseId, int dayValue, int monthValue, int year);

    @Query("SELECT w, w.fromWarehouse.name, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.action = ?1 AND w.fromWarehouse.id =?2 AND day(w.createdAt) = ?3  AND month(w.createdAt) = ?4 AND year(w.createdAt) = ?5  ORDER BY w.createdAt")
    List<Object[]> getAllWarehouseForWorkingReportByFromWarehouse(WarehouseProduct.Action kochirish, Long warehouseId, int dayValue, int monthValue, int year);

    @Query("SELECT w, w.toWarehouse.name, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.action = ?1 AND w.linkedWarehouse is null AND w.toWarehouse.id =?2 AND w.createdAt between ?3 and ?4 ORDER BY w.createdAt")
    List<Object[]> getAllWarehouseForWorkingReportByToWarehouseByDate(WarehouseProduct.Action kochirish, Long warehouseId, Date fromDate, Date toDate);

    @Query("SELECT w, w.fromWarehouse.name, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.action = ?1 AND w.fromWarehouse.id =?2 AND w.createdAt between ?3 and ?4  ORDER BY w.createdAt")
    List<Object[]> getAllWarehouseForWorkingReportByFromWarehouseByDate(WarehouseProduct.Action kochirish, Long warehouseId, Date fromDate, Date toDate);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.action = ?1 AND w.linkedWarehouse is null AND w.fromWarehouse.id IN(?2) AND w.fromWarehouse.isProduction is not true ORDER BY w.id DESC")
    List<Object[]> ownWithSummaAndQtyAndActionFromWarehouse(WarehouseProduct.Action kochirish, List<Long> ownWarehouses);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.action = ?1 AND w.linkedWarehouse is null AND w.fromWarehouse.id IN(?2) AND w.fromWarehouse.isProduction is true ORDER BY w.id DESC")
    List<Object[]> ownWithSummaAndQtyAndActionFromWarehouseProd(WarehouseProduct.Action kochirish, List<Long> ownWarehouses);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.documentStatus = ?1 AND w.action = ?2 AND w.linkedWarehouse is not null AND w.toWarehouse.id IN(?3) AND w.toWarehouse.isProduction is not true ORDER BY w.action ASC, w.id DESC")
    List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouse(WarehouseProduct.DocumentStatus ready, WarehouseProduct.Action kirim, List<Long> ownWarehouses);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE w.documentStatus = ?1 AND w.action = ?2 AND w.linkedWarehouse is not null AND w.toWarehouse.id IN(?3) AND w.toWarehouse.isProduction is true ORDER BY w.action ASC, w.id DESC")
    List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouseProd(WarehouseProduct.DocumentStatus ready, WarehouseProduct.Action kirim, List<Long> ownWarehouses);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id), " +
            "(SELECT SUM(ip.count * ip.incomePrice) FROM WarehouseProductItem ip WHERE ip.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2) AND w.action = ?3 AND w.toWarehouse.id IN(?4) ORDER BY w.action ASC, w.id DESC")
    List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouseNotLinked(WarehouseProduct.DocumentStatus waiting, WarehouseProduct.DocumentStatus cancelled, WarehouseProduct.Action kochirish, List<Long> ownWarehouses);

    @Query("SELECT w, (SELECT SUM(ic.count) FROM WarehouseProductItem ic WHERE ic.warehouseProduct.id = w.id) " +
            "FROM WarehouseProduct w WHERE (w.documentStatus = ?1 OR w.documentStatus = ?2) AND w.action = ?3 AND (w.fromWarehouse.id IN(?4) OR w.toWarehouse.id IN(?4)) ORDER BY w.action ASC, w.id DESC")
    List<Object[]> ownWithSummaAndQtyAndStatusAndActionFromWarehouseOrToWarehouseNotLinked(WarehouseProduct.DocumentStatus waiting, WarehouseProduct.DocumentStatus cancelled, WarehouseProduct.Action chiqim, List<Long> ownWarehouses);
}
