package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseExpense;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseExpenseRepository;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseExpenseService;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseExpenseServiceImpl implements WarehouseExpenseService {

    @Autowired
    WarehouseExpenseRepository warehouseExpenseRepository;

    @Override
    public List<WarehouseExpense> findAll() {
        return (List<WarehouseExpense>) warehouseExpenseRepository.findAll();
    }

    @Override
    public void saveWarehouseExpense(WarehouseExpense warehouseExpense) {
        warehouseExpenseRepository.save(warehouseExpense);
    }

    @Override
    public WarehouseExpense getWarehouseExpenseById(Long id) throws RecordNotFoundException {
        Optional<WarehouseExpense> warehouseExpenseOpt = warehouseExpenseRepository.findById(id);
        if (warehouseExpenseOpt.isPresent()) return warehouseExpenseOpt.get();
        else throw new RecordNotFoundException("No WarehouseExpense record exists for id: " + id);
    }

    @Override
    public void deleteWarehouseExpense(Long id) {
        warehouseExpenseRepository.deleteById(id);
    }
}
