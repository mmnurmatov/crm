package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseUser;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface WarehouseUserService {

    List<WarehouseUser> findAllWarehouseUsers();

    List<WarehouseUser> findAllProductionUsers();

    void saveWarehouseUser(WarehouseUser warehouseUser);

    WarehouseUser getWarehouseUserById(Long id) throws RecordNotFoundException;

    void deleteWarehouseUser(Long id);

    List<Warehouse> warehousesByUserId(Long userId);

    List<Warehouse> productionsByUserId(Long userId);

    List<Long> warehouseIdsByUserId(Long userId);

    List<Long> warehouseIds();

    List<Company> companiesByUserId(Long userId);
}
