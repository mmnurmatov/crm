package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseUser;

import java.util.List;

@Repository
public interface WarehouseUserRepository extends CrudRepository<WarehouseUser, Long> {
    @Query("SELECT wu.warehouse FROM WarehouseUser wu WHERE wu.user.id = ?1 AND wu.warehouse.isProduction IS NOT true ORDER BY wu.warehouse.name")
    List<Warehouse> warehousesByUserId(Long userId);

    @Query("SELECT wu.warehouse FROM WarehouseUser wu WHERE wu.user.id = ?1 AND wu.warehouse.isProduction IS true ORDER BY wu.warehouse.name")
    List<Warehouse> productionsByUserId(Long userId);

    @Query("SELECT wu.warehouse.id FROM WarehouseUser wu WHERE wu.user.id = ?1 ORDER BY wu.warehouse.name")
    List<Long> warehouseIdsByUserId(Long userId);

    @Query("SELECT wu.warehouse.id FROM WarehouseUser wu")
    List<Long> warehouseIds();

    @Query("SELECT wu.warehouse.company FROM WarehouseUser wu WHERE wu.user.id = ?1 ORDER BY wu.warehouse.company.name")
    List<Company> companiesByUserId(Long userId);

    @Query("SELECT wu FROM WarehouseUser wu WHERE wu.warehouse.isProduction IS true ORDER BY wu.warehouse.name")
    List<WarehouseUser> findAllProductionUsers();

    @Query("SELECT wu FROM WarehouseUser wu WHERE wu.warehouse.isProduction IS NOT TRUE ORDER BY wu.warehouse.name")
    List<WarehouseUser> findAllWarehouseUsers();
}
