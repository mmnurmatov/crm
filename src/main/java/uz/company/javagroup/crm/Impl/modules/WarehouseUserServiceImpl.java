package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseUser;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseUserRepository;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseUserService;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseUserServiceImpl implements WarehouseUserService {

    @Autowired
    WarehouseUserRepository warehouseUserRepository;

    @Override
    public List<WarehouseUser> findAllWarehouseUsers() {
        return warehouseUserRepository.findAllWarehouseUsers();
    }

    @Override
    public List<WarehouseUser> findAllProductionUsers() {
        return warehouseUserRepository.findAllProductionUsers();
    }

    @Override
    public void saveWarehouseUser(WarehouseUser warehouseUser) {
        warehouseUserRepository.save(warehouseUser);
    }

    @Override
    public WarehouseUser getWarehouseUserById(Long id) throws RecordNotFoundException {
        Optional<WarehouseUser> warehouseUserOpt = warehouseUserRepository.findById(id);
        if (warehouseUserOpt.isPresent()) return warehouseUserOpt.get();
        else throw new RecordNotFoundException("No WarehouseUser record exists for id: " + id);
    }

    @Override
    public void deleteWarehouseUser(Long id) {
        warehouseUserRepository.deleteById(id);
    }

    @Override
    public List<Warehouse> warehousesByUserId(Long userId) {
        return warehouseUserRepository.warehousesByUserId(userId);
    }

    @Override
    public List<Warehouse> productionsByUserId(Long userId) {
        return warehouseUserRepository.productionsByUserId(userId);
    }

    @Override
    public List<Long> warehouseIdsByUserId(Long userId) {
        return warehouseUserRepository.warehouseIdsByUserId(userId);
    }

    @Override
    public List<Long> warehouseIds() {
        return warehouseUserRepository.warehouseIds();
    }

    @Override
    public List<Company> companiesByUserId(Long userId) {
        return warehouseUserRepository.companiesByUserId(userId);
    }
}
