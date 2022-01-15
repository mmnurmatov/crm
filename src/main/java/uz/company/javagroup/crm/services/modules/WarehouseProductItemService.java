package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface WarehouseProductItemService {

    List<WarehouseProductItem> findAll();

    List<Object[]> findAllByWarehouseProductIdForWorkingReport(Long warehouseProductId);

    void saveWarehouseProductItem(WarehouseProductItem warehouseProductItem);

    WarehouseProductItem getWarehouseProductItemById(Long id) throws RecordNotFoundException;

    void deleteWarehouseProductItem(Long id);

    List<WarehouseProductItem> findAllByWarehouseProductId(Long whProductId);

    List<Object[]> findAllByWarehouseProductIdWithRemaining(Long whProductId);

    List<WarehouseProductItem> findAllNotZeroByWarehouseProductId(Long whProductId);
}
