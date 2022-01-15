package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseExpenseItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseExpenseItemRepository;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseExpenseItemService;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseExpenseItemServiceImpl implements WarehouseExpenseItemService {

    @Autowired
    WarehouseExpenseItemRepository warehouseExpenseItemRepository;

    @Override
    public List<WarehouseExpenseItem> findAll() {
        return (List<WarehouseExpenseItem>) warehouseExpenseItemRepository.findAll();
    }

    @Override
    public void saveWarehouseExpenseItem(WarehouseExpenseItem warehouseExpenseItem) {
        warehouseExpenseItemRepository.save(warehouseExpenseItem);
    }

    @Override
    public WarehouseExpenseItem getWarehouseExpenseItemById(Long id) throws RecordNotFoundException {
        Optional<WarehouseExpenseItem> warehouseExpenseItemOpt = warehouseExpenseItemRepository.findById(id);
        if (warehouseExpenseItemOpt.isPresent()) return warehouseExpenseItemOpt.get();
        else throw new RecordNotFoundException("No WarehouseExpenseItem record exists for id: " + id);
    }

    @Override
    public void deleteWarehouseExpenseItem(Long id) {
        warehouseExpenseItemRepository.deleteById(id);
    }

    @Override
    public List<WarehouseExpenseItem> findAllByWarehouseExpenseId(Long warehouseExpenseId) {
        return warehouseExpenseItemRepository.findAllByWarehouseExpenseId(warehouseExpenseId);
    }
}
