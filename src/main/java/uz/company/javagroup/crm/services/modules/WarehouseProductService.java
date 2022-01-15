package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public interface WarehouseProductService {

    List<WarehouseProduct> findAll();

    void saveWarehouseProduct(WarehouseProduct warehouseProduct);

    List<Object[]> getAllWarehouseForWorkingReportByToWarehouse(WarehouseProduct.Action kochirish, Long toWarehouseId, int dayValue, int monthValue, int year);

    List<Object[]> getAllWarehouseForWorkingReportByFromWarehouse(WarehouseProduct.Action kochirish, Long fromWarehouseId, int dayValue, int monthValue, int year);

    List<Object[]> getAllWarehouseForWorkingReportByToWarehouseByDate(WarehouseProduct.Action kochirish, Long toWarehouseId, Date fromDate, Date toDate);

    List<Object[]> getAllWarehouseForWorkingReportByFromWarehouseByDate(WarehouseProduct.Action kochirish, Long fromWarehouseId, Date fromDate, Date toDate);

    WarehouseProduct getWarehouseProductById(Long id) throws RecordNotFoundException;

    Optional<WarehouseProduct> findWarehouseProductById(String warehouseProductId);

    Optional<WarehouseProduct> findWarehouseProductByRegNumber(String regNumber);

    void deleteWarehouseProduct(Long id);

    Long findByToWarehouseIdAndAction(Long toWarehouseId, WarehouseProduct.Action action);

    Long findByFromWarehouseIdAndAction(Long fromWarehouseId, WarehouseProduct.Action action);

    List<Object[]> allWithSummaAndQty();

    List<Object[]> allWithSummaAndQtyAndAction(WarehouseProduct.Action kochirish);

    List<Object[]> ownWithSummaAndQtyAndActionToWarehouse(WarehouseProduct.Action kochirish, List<Long> ownWarehouses);

    List<Object[]> ownWithSummaAndQtyAndActionFromWarehouse(WarehouseProduct.Action kochirish, List<Long> ownWarehouses);

    List<Object[]> ownWithSummaAndQtyAndActionFromWarehouseProd(WarehouseProduct.Action kochirish, List<Long> ownWarehouses);

    List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouse(WarehouseProduct.DocumentStatus ready, WarehouseProduct.Action kirim, List<Long> ownWarehouses);

    List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouseProd(WarehouseProduct.DocumentStatus ready, WarehouseProduct.Action kirim, List<Long> ownWarehouses);

    List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouseNotLinked(WarehouseProduct.DocumentStatus waiting, WarehouseProduct.DocumentStatus cancelled, WarehouseProduct.Action kirim, List<Long> ownWarehouses);

    List<Object[]> ownWithSummaAndQtyAndStatusAndActionFromWarehouseOrToWarehouseNotLinked(WarehouseProduct.DocumentStatus waiting, WarehouseProduct.DocumentStatus cancelled, WarehouseProduct.Action kirim, List<Long> ownWarehouses);
}
