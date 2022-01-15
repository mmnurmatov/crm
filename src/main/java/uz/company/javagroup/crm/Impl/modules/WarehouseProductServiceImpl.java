package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseProductRepository;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseProductServiceImpl implements WarehouseProductService {

    @Autowired
    WarehouseProductRepository warehouseProductRepository;

    @Override
    public List<WarehouseProduct> findAll() {
        List<WarehouseProduct> warehousesProduct = (List<WarehouseProduct>) warehouseProductRepository.findAll();
        return warehousesProduct;
    }

    @Override
    public void saveWarehouseProduct(WarehouseProduct warehouseProduct) {
        warehouseProductRepository.save(warehouseProduct);
    }

    @Override
    public List<Object[]> getAllWarehouseForWorkingReportByToWarehouse(WarehouseProduct.Action kochirish, Long warehouseId, int dayValue, int monthValue, int year) {
        return warehouseProductRepository.getAllWarehouseForWorkingReportByToWarehouse(kochirish, warehouseId, dayValue, monthValue, year);
    }

    @Override
    public List<Object[]> getAllWarehouseForWorkingReportByFromWarehouse(WarehouseProduct.Action kochirish, Long warehouseId, int dayValue, int monthValue, int year) {
        return warehouseProductRepository.getAllWarehouseForWorkingReportByFromWarehouse(kochirish, warehouseId, dayValue, monthValue, year);
    }

    @Override
    public List<Object[]> getAllWarehouseForWorkingReportByToWarehouseByDate(WarehouseProduct.Action kochirish, Long warehouseId, Date fromDate, Date toDate) {
        return warehouseProductRepository.getAllWarehouseForWorkingReportByToWarehouseByDate(kochirish, warehouseId, fromDate, toDate);
    }

    @Override
    public List<Object[]> getAllWarehouseForWorkingReportByFromWarehouseByDate(WarehouseProduct.Action kochirish, Long warehouseId, Date fromDate, Date toDate) {
        return warehouseProductRepository.getAllWarehouseForWorkingReportByFromWarehouseByDate(kochirish, warehouseId, fromDate, toDate);
    }

    @Override
    public WarehouseProduct getWarehouseProductById(Long id) throws RecordNotFoundException {
        Optional<WarehouseProduct> warehouseProductOpt = warehouseProductRepository.findById(id);
        if (warehouseProductOpt.isPresent()) return warehouseProductOpt.get();
        else throw new RecordNotFoundException("No WarehouseProduct record exists for id: " + id);
    }

    @Override
    public Optional<WarehouseProduct> findWarehouseProductById(String warehouseProductId) {
        return warehouseProductRepository.findWarehouseProductById(warehouseProductId);
    }

    @Override
    public Optional<WarehouseProduct> findWarehouseProductByRegNumber(String regNumber) {
        return warehouseProductRepository.findWarehouseProductByRegNumber(regNumber);
    }

    @Override
    public void deleteWarehouseProduct(Long id) {
        warehouseProductRepository.deleteById(id);
    }

    @Override
    public Long findByToWarehouseIdAndAction(Long toWarehouseId, WarehouseProduct.Action action) {
        return warehouseProductRepository.findByToWarehouseIdAndAction(toWarehouseId, action);
    }

    @Override
    public Long findByFromWarehouseIdAndAction(Long fromWarehouseId, WarehouseProduct.Action action) {
        return warehouseProductRepository.findByFromWarehouseIdAndAction(fromWarehouseId, action);
    }

    @Override
    public List<Object[]> allWithSummaAndQty() {
        return warehouseProductRepository.allWithSummaAndQty();
    }

    @Override
    public List<Object[]> allWithSummaAndQtyAndAction(WarehouseProduct.Action action) {
        return warehouseProductRepository.allWithSummaAndQtyAndAction(action);
    }

    @Override
    public List<Object[]> ownWithSummaAndQtyAndActionToWarehouse(WarehouseProduct.Action kochirish, List<Long> ownWarehouses) {
        return warehouseProductRepository.ownWithSummaAndQtyAndActionToWarehouse(kochirish, ownWarehouses);
    }

    @Override
    public List<Object[]> ownWithSummaAndQtyAndActionFromWarehouse(WarehouseProduct.Action kochirish, List<Long> ownWarehouses) {
        return warehouseProductRepository.ownWithSummaAndQtyAndActionFromWarehouse(kochirish, ownWarehouses);
    }

    @Override
    public List<Object[]> ownWithSummaAndQtyAndActionFromWarehouseProd(WarehouseProduct.Action kochirish, List<Long> ownWarehouses) {
        return warehouseProductRepository.ownWithSummaAndQtyAndActionFromWarehouseProd(kochirish, ownWarehouses);
    }

    @Override
    public List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouse(WarehouseProduct.DocumentStatus ready, WarehouseProduct.Action kirim, List<Long> ownWarehouses) {
        return warehouseProductRepository.ownWithSummaAndQtyAndStatusAndActionToWarehouse(ready, kirim, ownWarehouses);
    }

    @Override
    public List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouseProd(WarehouseProduct.DocumentStatus ready, WarehouseProduct.Action kirim, List<Long> ownWarehouses) {
        return warehouseProductRepository.ownWithSummaAndQtyAndStatusAndActionToWarehouseProd(ready, kirim, ownWarehouses);
    }

    @Override
    public List<Object[]> ownWithSummaAndQtyAndStatusAndActionToWarehouseNotLinked(WarehouseProduct.DocumentStatus waiting, WarehouseProduct.DocumentStatus cancelled, WarehouseProduct.Action kochirish, List<Long> ownWarehouses) {
        return warehouseProductRepository.ownWithSummaAndQtyAndStatusAndActionToWarehouseNotLinked(waiting, cancelled, kochirish, ownWarehouses);
    }

    @Override
    public List<Object[]> ownWithSummaAndQtyAndStatusAndActionFromWarehouseOrToWarehouseNotLinked(WarehouseProduct.DocumentStatus waiting, WarehouseProduct.DocumentStatus cancelled, WarehouseProduct.Action chiqim, List<Long> ownWarehouses) {
        return warehouseProductRepository.ownWithSummaAndQtyAndStatusAndActionFromWarehouseOrToWarehouseNotLinked(waiting, cancelled, chiqim, ownWarehouses);
    }
}
