package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.ItemBalance;
import uz.isd.javagroup.grandcrm.utility.ProductDto;
import uz.isd.javagroup.grandcrm.utility.ProductPriceAndCountDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemBalanceRepository extends CrudRepository<ItemBalance, Long> {
    Optional<ItemBalance> findFirstByProductIdAndWarehouseIdOrderByIdDesc(Long itemId, Long depId);

    @Query(value = "select p.id as productid, p.name_uz as productnameuz, p.name_en as productnameen, p.name_ru as productnameru, u.id as unitid, u.name_uz as unitnameuz, u.name_en as unitnameen, u.name_ru as unitnameru from items_balance b left join products p ON p.id = b.product_id left join product_units u ON u.id = p.product_unit_id where b.warehouse_id = ?1 group by p.id, p.name_uz, p.name_en, p.name_ru, u.id, u.name_uz, u.name_en, u.name_ru", nativeQuery = true)
    List<ProductDto> getAllProductByWarehouseId(Long warehouseId);

    @Query(value = "select b.count as productcount, b.income_price as incomeprice\n" +
            "from items_balance b\n" +
            "where b.warehouse_id = ?1\n" +
            "  and b.product_id = ?2\n" +
            "  and b.reg_number like concat(concat('%', ?3), '%')\n" +
            "  and rownum = 1\n" +
            "order by b.id desc", nativeQuery = true)
    ProductPriceAndCountDto getPriceAndCount(Long warehouseId, Long productId, String action);
}
