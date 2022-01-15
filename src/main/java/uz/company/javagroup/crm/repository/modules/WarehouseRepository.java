package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;

import java.util.List;

@Repository
public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {
    @Query(value = "select * from WAREHOUSES where ID != ?1", nativeQuery = true)
    List<Warehouse> getAllWarehousesWithoutSelectedWarehouseId(Long warehouseId);

    @Query("SELECT p FROM Warehouse p WHERE p.isProduction IS NOT true")
    List<Warehouse> findAllWarehouses();

    @Query("SELECT p FROM Warehouse p WHERE p.isProduction IS true")
    List<Warehouse> findAllProductions();

    @Query("SELECT w FROM Warehouse w WHERE w.company.id IN(SELECT p.company.id FROM Warehouse p WHERE p.id = ?1) AND w.isProduction IS NOT true")
    List<Warehouse> getAllCompanyWarehousesWithProductionId(Long productionId);

    @Query("SELECT w FROM Warehouse w WHERE w.company.id = ?1")
    List<Warehouse> findAllWarehousesByCompanyId(Long companyId);
}
