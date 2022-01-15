package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Conversion;
import uz.isd.javagroup.grandcrm.entity.modules.ConversionItem;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface ItemStatusService {
    void countIncomeWhDoc(WarehouseProduct warehouseProduct);

    void countIncomeWhDocLocal(WarehouseProduct warehouseProduct);

    void countTransferWhDoc(WarehouseProduct warehouseProduct) throws RecordNotFoundException;

    void countTransferWhDocWithRequisite(WarehouseProduct warehouseProduct) throws RecordNotFoundException;

    void countExpense(WarehouseProduct warehouseProduct);

    void subtractConversionItem(ConversionItem conversionItem) throws RecordNotFoundException;

    void addConversion(Conversion conversion);

    List<Object[]> warehouseBalance(Long warehouseId);

    List<Object[]> allByWarehouse(Long warehouseId, int i);

    List<Object[]> allByColumnByWarehouse(Long warehouseId, String keyword, int i);
}
