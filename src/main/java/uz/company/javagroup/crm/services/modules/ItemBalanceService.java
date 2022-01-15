package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.utility.ProductPriceAndCountDto;
import uz.isd.javagroup.grandcrm.utility.ReportOneDto;

import java.util.List;

@Service
public interface ItemBalanceService {
    void countIncomeWhDoc(WarehouseProduct warehouseProduct, WarehouseProductItem whDocumentItem);

    void countTransferWhDoc(WarehouseProduct warehouseProduct, WarehouseProductItem whDocumentItem);

    void addConversion(Conversion conversion);

    void subtractConversionItem(ConversionItem conversionItem);

    List<ReportOneDto> getAllProductByWarehouseId(Long warehouseId);

    ProductPriceAndCountDto getPriceAndCount(Long warehouseId, Long productId, String action);
}
