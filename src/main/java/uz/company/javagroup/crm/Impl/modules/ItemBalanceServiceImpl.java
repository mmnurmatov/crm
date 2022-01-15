package uz.isd.javagroup.grandcrm.Impl.modules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.repository.modules.ItemBalanceRepository;
import uz.isd.javagroup.grandcrm.services.modules.ItemBalanceService;
import uz.isd.javagroup.grandcrm.utility.ProductDto;
import uz.isd.javagroup.grandcrm.utility.ProductPriceAndCountDto;
import uz.isd.javagroup.grandcrm.utility.ReportOneDto;
import uz.isd.javagroup.grandcrm.utility.Utils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ItemBalanceServiceImpl implements ItemBalanceService {

    @Autowired
    EntityManager entityManager;
    @Autowired
    ItemBalanceRepository itemBalanceRepository;

    @Override
    public void countIncomeWhDoc(WarehouseProduct warehouseProduct, WarehouseProductItem whDocumentItem) {
        Optional<ItemBalance> lastItemBalance = itemBalanceRepository.findFirstByProductIdAndWarehouseIdOrderByIdDesc(whDocumentItem.getProduct().getId(), warehouseProduct.getToWarehouse().getId());

        ItemBalance itemBalance = new ItemBalance();
        itemBalance.setRegDate(new Date());
        itemBalance.setRegNumber(warehouseProduct.getRegNumber());
        itemBalance.setWarehouseProduct(warehouseProduct);
        itemBalance.setWarehouse(warehouseProduct.getToWarehouse());
        itemBalance.setProduct(whDocumentItem.getProduct());

        itemBalance.setCount(whDocumentItem.getCount());
        itemBalance.setIncomePrice(whDocumentItem.getIncomePrice());
        if (Utils.isEmpty(whDocumentItem.getPrice())) itemBalance.setPrice(BigDecimal.ZERO);
        else itemBalance.setPrice(whDocumentItem.getPrice());
        if (Utils.isEmpty(whDocumentItem.getPriceWholesale())) itemBalance.setPriceWholesale(BigDecimal.ZERO);
        else itemBalance.setPriceWholesale(whDocumentItem.getPriceWholesale());
        itemBalance.setAmount(itemBalance.getCount().multiply(itemBalance.getPrice()));
        itemBalance.setAmountWholesale(itemBalance.getCount().multiply(itemBalance.getPriceWholesale()));
        if (!lastItemBalance.isPresent()) itemBalance.setRemaining(whDocumentItem.getCount());
        else itemBalance.setRemaining(lastItemBalance.get().getRemaining().add(whDocumentItem.getCount()));
        itemBalanceRepository.save(itemBalance);
    }

    @Override
    public void countTransferWhDoc(WarehouseProduct warehouseProduct, WarehouseProductItem whDocumentItem) {
        Warehouse warehouse = warehouseProduct.getFromWarehouse();
        Optional<ItemBalance> lastItemBalance = itemBalanceRepository.findFirstByProductIdAndWarehouseIdOrderByIdDesc(whDocumentItem.getProduct().getId(), warehouse.getId());
        if (!lastItemBalance.isPresent()) return;

        ItemBalance itemBalance = new ItemBalance();
        itemBalance.setRegDate(new Date());
        itemBalance.setRegNumber(warehouseProduct.getRegNumber());
        itemBalance.setWarehouseProduct(warehouseProduct);
        itemBalance.setWarehouse(warehouse);
        itemBalance.setProduct(whDocumentItem.getProduct());

        itemBalance.setCount(whDocumentItem.getCount().multiply(new BigDecimal(-1)));
        itemBalance.setIncomePrice(whDocumentItem.getIncomePrice());
        if (Utils.isEmpty(whDocumentItem.getPrice())) itemBalance.setPrice(BigDecimal.ZERO);
        else itemBalance.setPrice(whDocumentItem.getPrice());
        if (Utils.isEmpty(whDocumentItem.getPriceWholesale())) itemBalance.setPriceWholesale(BigDecimal.ZERO);
        else itemBalance.setPriceWholesale(whDocumentItem.getPriceWholesale());
        itemBalance.setAmount(itemBalance.getCount().multiply(itemBalance.getPrice()));
        itemBalance.setAmountWholesale(itemBalance.getCount().multiply(itemBalance.getPriceWholesale()));
        itemBalance.setRemaining(lastItemBalance.get().getRemaining().subtract(whDocumentItem.getCount()));
        itemBalanceRepository.save(itemBalance);
    }

    @Override
    public void addConversion(Conversion conversion) {
        Optional<ItemBalance> lastItemBalance = itemBalanceRepository.findFirstByProductIdAndWarehouseIdOrderByIdDesc(conversion.getProduct().getId(), conversion.getProduction().getId());

        ItemBalance itemBalance = new ItemBalance();
        itemBalance.setRegDate(new Date());
        itemBalance.setWarehouse(conversion.getProduction());
        itemBalance.setProduct(conversion.getProduct());

        itemBalance.setCount(conversion.getCount());
        itemBalance.setIncomePrice(conversion.getPrice());
        if (Utils.isEmpty(conversion.getPrice())) itemBalance.setPrice(BigDecimal.ZERO);
        else itemBalance.setPrice(conversion.getPrice());
        if (Utils.isEmpty(conversion.getPrice())) itemBalance.setPriceWholesale(BigDecimal.ZERO);
        else itemBalance.setPriceWholesale(conversion.getPrice());
        itemBalance.setAmount(itemBalance.getCount().multiply(itemBalance.getPrice()));
        itemBalance.setAmountWholesale(itemBalance.getCount().multiply(itemBalance.getPriceWholesale()));
        if (!lastItemBalance.isPresent()) itemBalance.setRemaining(conversion.getCount());
        else itemBalance.setRemaining(lastItemBalance.get().getRemaining().add(conversion.getCount()));
        itemBalanceRepository.save(itemBalance);
    }

    @Override
    public void subtractConversionItem(ConversionItem conversionItem) {
        Warehouse production = conversionItem.getConversion().getProduction();
        Optional<ItemBalance> lastItemBalance = itemBalanceRepository.findFirstByProductIdAndWarehouseIdOrderByIdDesc(conversionItem.getProduct().getId(), production.getId());
        if (!lastItemBalance.isPresent()) return;

        ItemBalance itemBalance = new ItemBalance();
        itemBalance.setRegDate(new Date());
        itemBalance.setWarehouse(production);
        itemBalance.setProduct(conversionItem.getProduct());

        itemBalance.setCount(conversionItem.getCount().multiply(new BigDecimal(-1)));
        itemBalance.setIncomePrice(conversionItem.getPrice());
        if (Utils.isEmpty(conversionItem.getPrice())) itemBalance.setPrice(BigDecimal.ZERO);
        else itemBalance.setPrice(conversionItem.getPrice());
        if (Utils.isEmpty(conversionItem.getPrice())) itemBalance.setPriceWholesale(BigDecimal.ZERO);
        else itemBalance.setPriceWholesale(conversionItem.getPrice());
        itemBalance.setAmount(itemBalance.getCount().multiply(itemBalance.getPrice()));
        itemBalance.setAmountWholesale(itemBalance.getCount().multiply(itemBalance.getPriceWholesale()));
        itemBalance.setRemaining(lastItemBalance.get().getRemaining().subtract(conversionItem.getCount()));
        itemBalanceRepository.save(itemBalance);
    }

    @Override
    public List<ReportOneDto> getAllProductByWarehouseId(Long warehouseId) {
        List<ProductDto> getProducts = itemBalanceRepository.getAllProductByWarehouseId(warehouseId);
        log.info("Product DTO into Report {}", getProducts);
        List<ReportOneDto> response = new ArrayList<>();
        for (ProductDto p: getProducts) {
            log.info("Product data {}", p);
            ProductPriceAndCountDto balanceForIncome = getPriceAndCount(warehouseId, p.getProductId(), "In");
            ProductPriceAndCountDto balanceForExpanse = getPriceAndCount(warehouseId, p.getProductId(), "Ex");
            ReportOneDto data = new ReportOneDto();
            data.setProductId(p.getProductId());
            data.setUnitId(p.getUnitId());
            data.setUnitNameUz(p.getUnitNameUz());
            data.setUnitNameEn(p.getUnitNameEn());
            data.setUnitNameRu(p.getUnitNameRu());
            data.setProductNameUz(p.getProductNameUz());
            data.setProductNameRu(p.getProductNameRu());
            data.setProductNameEn(p.getProductNameEn());
            if(balanceForExpanse != null){
                data.setExpanseCount(balanceForExpanse.getProductCount());
                data.setExpansePrice(balanceForExpanse.getIncomePrice().multiply(new BigDecimal(-1)));
            }
            if(balanceForIncome != null){
                data.setIncomeCount(balanceForIncome.getProductCount());
                data.setIncomePrice(balanceForIncome.getIncomePrice().multiply(new BigDecimal(-1)));
            }
            response.add(data);
        }
        return response;
    }


    public ProductPriceAndCountDto getPriceAndCount(Long warehouseId, Long productId, String action) {
       return itemBalanceRepository.getPriceAndCount(warehouseId, productId, action);
    }
}
