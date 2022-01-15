package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface WarehouseService {

    List<Warehouse> findAll();

    void saveWarehouse(Warehouse warehouse);

    Warehouse getWarehouseById(Long id) throws RecordNotFoundException;

    void deleteWarehouse(Long id);

    List<Warehouse> getAllWarehousesWithoutSelectedWarehouseId(Long warehouseId);

    List<Warehouse> findAllProductions();

    List<Warehouse> getAllCompanyWarehousesWithProductionId(Long productionId);

    List<Warehouse> findAllWarehousesByCompanyId(Long companyId);
}
