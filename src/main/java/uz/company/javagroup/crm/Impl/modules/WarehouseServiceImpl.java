package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRepository;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAllWarehouses();
    }

    @Override
    public List<Warehouse> findAllProductions() {
        return warehouseRepository.findAllProductions();
    }

    @Override
    public List<Warehouse> getAllCompanyWarehousesWithProductionId(Long productionId) {
        return warehouseRepository.getAllCompanyWarehousesWithProductionId(productionId);
    }

    @Override
    public void saveWarehouse(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    @Override
    public Warehouse getWarehouseById(Long id) throws RecordNotFoundException {
        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(id);
        if (warehouseOpt.isPresent()) return warehouseOpt.get();
        else throw new RecordNotFoundException("No Warehouse record exists for id: " + id);
    }

    @Override
    public void deleteWarehouse(Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public List<Warehouse> getAllWarehousesWithoutSelectedWarehouseId(Long warehouseId) {
        return warehouseRepository.getAllWarehousesWithoutSelectedWarehouseId(warehouseId);
    }

    @Override
    public List<Warehouse> findAllWarehousesByCompanyId(Long companyId) {
        return warehouseRepository.findAllWarehousesByCompanyId(companyId);
    }
}
