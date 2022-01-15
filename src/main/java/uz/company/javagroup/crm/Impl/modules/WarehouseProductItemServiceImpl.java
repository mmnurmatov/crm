package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseProductItemRepository;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductItemService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseProductItemServiceImpl implements WarehouseProductItemService {

    @Autowired
    WarehouseProductItemRepository warehouseProductItemRepository;

    @Override
    public List<WarehouseProductItem> findAll() {
        List<WarehouseProductItem> warehouseProductItems = (List<WarehouseProductItem>) warehouseProductItemRepository.findAll();
        return warehouseProductItems;
    }

    @Override
    public List<Object[]> findAllByWarehouseProductIdForWorkingReport(Long warehouseProductId) {
        return warehouseProductItemRepository.findAllByWarehouseProductIdForWorkingReport(warehouseProductId);
    }

    @Override
    public void saveWarehouseProductItem(WarehouseProductItem warehouseProductItem) {
        warehouseProductItemRepository.save(warehouseProductItem);
    }

    @Override
    public WarehouseProductItem getWarehouseProductItemById(Long id) throws RecordNotFoundException {
        Optional<WarehouseProductItem> warehouseProductItemOpt = warehouseProductItemRepository.findById(id);
        if (warehouseProductItemOpt.isPresent()) return warehouseProductItemOpt.get();
        else throw new RecordNotFoundException("No WarehouseProductItem record exists for id: " + id);
    }

    @Override
    public void deleteWarehouseProductItem(Long id) {
        warehouseProductItemRepository.deleteById(id);
    }

    @Override
    public List<WarehouseProductItem> findAllByWarehouseProductId(Long warehouseProductId) {
        return warehouseProductItemRepository.findAllByWarehouseProductId(warehouseProductId);
    }

    @Override
    public List<Object[]> findAllByWarehouseProductIdWithRemaining(Long warehouseProductId) {
        return warehouseProductItemRepository.findAllByWarehouseProductIdWithRemaining(warehouseProductId);
    }

    @Override
    public List<WarehouseProductItem> findAllNotZeroByWarehouseProductId(Long warehouseProductId) {
        return warehouseProductItemRepository.findAllByWarehouseProductIdAndRemainingIsGreaterThan(warehouseProductId, BigDecimal.ZERO);
    }
}
