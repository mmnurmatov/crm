package uz.isd.javagroup.grandcrm.Impl.modules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.ItemStatusRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseProductItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseProductRepository;
import uz.isd.javagroup.grandcrm.services.modules.ItemBalanceService;
import uz.isd.javagroup.grandcrm.services.modules.ItemStatusService;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.*;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentStatus.READY;

@Slf4j
@Service
public class ItemStatusServiceImpl implements ItemStatusService {

    @Autowired
    ItemStatusRepository itemStatusRepository;
    @Autowired
    ItemBalanceService itemBalanceService;
    @Autowired
    WarehouseProductRepository warehouseProductRepository;
    @Autowired
    WarehouseProductItemRepository whProductItemRepository;

    @Override
    public void countIncomeWhDoc(WarehouseProduct warehouseProduct) {
        if (warehouseProduct == null || warehouseProduct.getDocumentStatus() != READY || warehouseProduct.getAction() != KIRIM)
            return;
        try {
            List<WarehouseProductItem> whDocumentItems = whProductItemRepository.findAllByWarehouseProductId(warehouseProduct.getId());
            for (WarehouseProductItem whDocumentItem : whDocumentItems) {
                Optional<ItemStatus> itemStatusOptional = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndWarehouseProductId(whDocumentItem.getProduct().getId(), warehouseProduct.getToWarehouse().getId(), warehouseProduct.getId());
                if (itemStatusOptional.isPresent() && !Utils.isEmpty(itemStatusOptional.get())) {
                    itemStatusOptional.get().setRemaining(itemStatusOptional.get().getRemaining().add(whDocumentItem.getCount()));
                    if (whDocumentItem.getIncomePrice() != null)
                        itemStatusOptional.get().setIncomePrice(whDocumentItem.getIncomePrice());
                    if (whDocumentItem.getSector() != null)
                        itemStatusOptional.get().setSector(whDocumentItem.getSector().getId());
                    itemStatusRepository.save(itemStatusOptional.get());
                } else {
                    ItemStatus itemStatus = new ItemStatus();
                    itemStatus.setRemaining(whDocumentItem.getCount());
                    itemStatus.setWarehouseProduct(warehouseProduct);
                    itemStatus.setWarehouse(warehouseProduct.getToWarehouse());
                    itemStatus.setProduct(whDocumentItem.getProduct());
                    if (whDocumentItem.getIncomePrice() != null)
                        itemStatus.setIncomePrice(whDocumentItem.getIncomePrice());
                    if (Utils.isEmpty(whDocumentItem.getPrice())) itemStatus.setPrice(BigDecimal.ZERO);
                    else itemStatus.setPrice(whDocumentItem.getPrice());
                    if (Utils.isEmpty(whDocumentItem.getPriceWholesale()))
                        itemStatus.setPriceWholesale(BigDecimal.ZERO);
                    else itemStatus.setPriceWholesale(whDocumentItem.getPriceWholesale());
                    if (whDocumentItem.getSector() != null)
                        itemStatus.setSector(whDocumentItem.getSector().getId());
                    itemStatusRepository.save(itemStatus);
                }
                itemBalanceService.countIncomeWhDoc(warehouseProduct, whDocumentItem);
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
    }

    @Override
    public void countIncomeWhDocLocal(WarehouseProduct warehouseProduct) {
        if (warehouseProduct == null || warehouseProduct.getDocumentStatus() != READY || warehouseProduct.getAction() != KIRIM)
            return;
        try {
            List<WarehouseProductItem> whDocumentItems = warehouseProduct.getWhItems();
            for (WarehouseProductItem whDocumentItem : whDocumentItems) {
                Optional<ItemStatus> itemStatusOptional = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndWarehouseProductId(whDocumentItem.getProduct().getId(), warehouseProduct.getToWarehouse().getId(), warehouseProduct.getId());
                if (itemStatusOptional.isPresent() && !Utils.isEmpty(itemStatusOptional.get())) {
                    itemStatusOptional.get().setRemaining(itemStatusOptional.get().getRemaining().add(whDocumentItem.getCount()));
                    if (whDocumentItem.getIncomePrice() != null)
                        itemStatusOptional.get().setIncomePrice(whDocumentItem.getIncomePrice());
                    if (whDocumentItem.getSector() != null)
                        itemStatusOptional.get().setSector(whDocumentItem.getSector().getId());
                    itemStatusRepository.save(itemStatusOptional.get());
                } else {
                    ItemStatus itemStatus = new ItemStatus();
                    itemStatus.setRemaining(whDocumentItem.getCount());
                    itemStatus.setWarehouseProduct(warehouseProduct);
                    itemStatus.setWarehouse(warehouseProduct.getToWarehouse());
                    itemStatus.setProduct(whDocumentItem.getProduct());
                    if (whDocumentItem.getIncomePrice() != null)
                        itemStatus.setIncomePrice(whDocumentItem.getIncomePrice());
                    if (Utils.isEmpty(whDocumentItem.getPrice())) itemStatus.setPrice(BigDecimal.ZERO);
                    else itemStatus.setPrice(whDocumentItem.getPrice());
                    if (Utils.isEmpty(whDocumentItem.getPriceWholesale()))
                        itemStatus.setPriceWholesale(BigDecimal.ZERO);
                    else itemStatus.setPriceWholesale(whDocumentItem.getPriceWholesale());
                    if (whDocumentItem.getSector() != null)
                        itemStatus.setSector(whDocumentItem.getSector().getId());
                    itemStatusRepository.save(itemStatus);
                }
                itemBalanceService.countIncomeWhDoc(warehouseProduct, whDocumentItem);
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
    }

    @Override
    public void countTransferWhDoc(WarehouseProduct warehouseProduct) throws RecordNotFoundException {
        if (warehouseProduct.getDocumentStatus() != READY && (warehouseProduct.getAction() != KOCHIRISH || warehouseProduct.getAction() != SPISAT || warehouseProduct.getAction() != CHIQIM))
            throw new RecordNotFoundException("Invalid KOCHIRISH...");
        List<WarehouseProductItem> whDocumentItems = whProductItemRepository.findAllByWarehouseProductId(warehouseProduct.getId());
        Warehouse warehouse = warehouseProduct.getFromWarehouse();
        Warehouse warehouse2nd;
        if (warehouseProduct.getAction() == SPISAT) warehouse2nd = warehouseProduct.getFromWarehouse();
        else warehouse2nd = warehouseProduct.getToWarehouse();
        for (WarehouseProductItem whDocumentItem : whDocumentItems) {
            BigDecimal count = whDocumentItem.getCount();
            while (count.compareTo(BigDecimal.ZERO) > 0) {
                Optional<ItemStatus> itemStatusOpt = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndIncomePriceAndRemainingGreaterThan(whDocumentItem.getProduct().getId(), warehouse.getId(), whDocumentItem.getIncomePrice(), BigDecimal.ZERO);
                if (!itemStatusOpt.isPresent()) throw new RecordNotFoundException("ItemStatus not found");
//                    if (warehouse2nd.getCompany().getSaleType() == RETAIL)
//                        itemStatusOpt = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndPrice(whDocumentItem.getProduct().getId(), warehouse.getId(), whDocumentItem.getPrice());
//                    else
//                        itemStatusOpt = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndPriceWholesale(whDocumentItem.getProduct().getId(), warehouse.getId(), whDocumentItem.getPriceWholesale());
//                    if (!itemStatusOpt.isPresent()) return;
                if (count.compareTo(itemStatusOpt.get().getRemaining()) == 0) {
                    itemStatusOpt.get().setRemaining(BigDecimal.ZERO);
                    count = BigDecimal.ZERO;
                } else if (itemStatusOpt.get().getRemaining().compareTo(count) > 0) {
                    itemStatusOpt.get().setRemaining(itemStatusOpt.get().getRemaining().subtract(count));
                    count = BigDecimal.ZERO;
                } else if (count.compareTo(itemStatusOpt.get().getRemaining()) > 0) {
                    count = count.subtract(itemStatusOpt.get().getRemaining());
                    itemStatusOpt.get().setRemaining(BigDecimal.ZERO);
                }
                itemStatusRepository.save(itemStatusOpt.get());
            }
            itemBalanceService.countTransferWhDoc(warehouseProduct, whDocumentItem);
        }
    }

    @Override
    public void subtractConversionItem(ConversionItem conversionItem) throws RecordNotFoundException {
        BigDecimal count = conversionItem.getCount();
        if (count.compareTo(BigDecimal.ZERO) == 0 || count.compareTo(BigDecimal.ZERO) < 0)
            throw new RecordNotFoundException("Invalid count...");
        while (count.compareTo(BigDecimal.ZERO) > 0) {
            Optional<ItemStatus> itemStatusOpt = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndIncomePriceAndRemainingGreaterThan(conversionItem.getProduct().getId(), conversionItem.getConversion().getProduction().getId(), conversionItem.getPrice(), BigDecimal.ZERO);
            if (!itemStatusOpt.isPresent()) throw new RecordNotFoundException("ItemStatus not found");
            if (count.compareTo(itemStatusOpt.get().getRemaining()) == 0) {
                itemStatusOpt.get().setRemaining(BigDecimal.ZERO);
                count = BigDecimal.ZERO;
            } else if (itemStatusOpt.get().getRemaining().compareTo(count) > 0) {
                itemStatusOpt.get().setRemaining(itemStatusOpt.get().getRemaining().subtract(count));
                count = BigDecimal.ZERO;
            } else if (count.compareTo(itemStatusOpt.get().getRemaining()) > 0) {
                count = count.subtract(itemStatusOpt.get().getRemaining());
                itemStatusOpt.get().setRemaining(BigDecimal.ZERO);
            }
            itemStatusRepository.save(itemStatusOpt.get());
        }
        itemBalanceService.subtractConversionItem(conversionItem);
    }

    @Override
    public void addConversion(Conversion conversion) {
        ItemStatus itemStatus = new ItemStatus();
        itemStatus.setRemaining(conversion.getCount());
        itemStatus.setWarehouse(conversion.getProduction());
        itemStatus.setProduct(conversion.getProduct());
        if (conversion.getPrice() != null) itemStatus.setIncomePrice(conversion.getPrice());
        if (Utils.isEmpty(conversion.getPrice())) itemStatus.setPrice(BigDecimal.ZERO);
        else itemStatus.setPrice(conversion.getPrice());
        if (Utils.isEmpty(conversion.getPrice()))
            itemStatus.setPriceWholesale(BigDecimal.ZERO);
        else itemStatus.setPriceWholesale(conversion.getPrice());
        itemStatusRepository.save(itemStatus);
        itemBalanceService.addConversion(conversion);
    }

    @Override
    public void countTransferWhDocWithRequisite(WarehouseProduct warehouseProduct) throws RecordNotFoundException {
        if (warehouseProduct.getDocumentStatus() != READY && (warehouseProduct.getAction() != KOCHIRISH || warehouseProduct.getAction() != SPISAT || warehouseProduct.getAction() != CHIQIM))
            throw new RecordNotFoundException("Invalid KOCHIRISH...");
        List<WarehouseProductItem> whDocumentItems = whProductItemRepository.findAllByWarehouseProductId(warehouseProduct.getId());
        Warehouse warehouse = warehouseProduct.getFromWarehouse();
        for (WarehouseProductItem whDocumentItem : whDocumentItems) {
            BigDecimal count = whDocumentItem.getCount();
            while (count.compareTo(BigDecimal.ZERO) > 0) {
                Long productId = whDocumentItem.getProduct().getId();
                Long warehouseId = warehouse.getId();
                ItemStatus itemStatusOpt = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndRemainingGreaterThan(productId, warehouseId, PageRequest.of(0, 1));
                if (count.compareTo(itemStatusOpt.getRemaining()) == 0) {
                    itemStatusOpt.setRemaining(BigDecimal.ZERO);
                    count = BigDecimal.ZERO;
                } else if (itemStatusOpt.getRemaining().compareTo(count) > 0) {
                    itemStatusOpt.setRemaining(itemStatusOpt.getRemaining().subtract(count));
                    count = BigDecimal.ZERO;
                } else if (count.compareTo(itemStatusOpt.getRemaining()) > 0) {
                    count = count.subtract(itemStatusOpt.getRemaining());
                    itemStatusOpt.setRemaining(BigDecimal.ZERO);
                }
                itemStatusRepository.save(itemStatusOpt);
            }
            itemBalanceService.countTransferWhDoc(warehouseProduct, whDocumentItem);
        }
    }

    @Override
    public void countExpense(WarehouseProduct warehouseProduct) {
        if (warehouseProduct.getAction() != CHIQIM)
            return;
        List<WarehouseProductItem> whDocumentItems = whProductItemRepository.findAllByWarehouseProductId(warehouseProduct.getId());
        Warehouse warehouse = warehouseProduct.getFromWarehouse();
        for (WarehouseProductItem whDocumentItem : whDocumentItems) {
            BigDecimal count = whDocumentItem.getCount();
            while (count.compareTo(BigDecimal.ZERO) > 0) {
                Optional<ItemStatus> itemStatusOpt = itemStatusRepository.findFirstByProductIdAndWarehouseIdAndIncomePriceAndRemainingGreaterThan(whDocumentItem.getProduct().getId(), warehouse.getId(), whDocumentItem.getIncomePrice(), BigDecimal.ZERO);
                if (count.compareTo(itemStatusOpt.get().getRemaining()) == 0) {
                    itemStatusOpt.get().setRemaining(BigDecimal.ZERO);
                    count = BigDecimal.ZERO;
                } else if (itemStatusOpt.get().getRemaining().compareTo(count) > 0) {
                    itemStatusOpt.get().setRemaining(itemStatusOpt.get().getRemaining().subtract(count));
                    count = BigDecimal.ZERO;
                } else if (count.compareTo(itemStatusOpt.get().getRemaining()) > 0) {
                    count = count.subtract(itemStatusOpt.get().getRemaining());
                    itemStatusOpt.get().setRemaining(BigDecimal.ZERO);
                }
                itemStatusRepository.save(itemStatusOpt.get());
            }
            itemBalanceService.countTransferWhDoc(warehouseProduct, whDocumentItem);
        }
    }

    @Override
    public List<Object[]> warehouseBalance(Long warehouseId) {
        return itemStatusRepository.warehouseBalance(warehouseId);
    }

    @Override
    public List<Object[]> allByWarehouse(Long warehouseId, int i) {
        return itemStatusRepository.allByWarehouse(warehouseId, PageRequest.of(0, i));
    }

    @Override
    public List<Object[]> allByColumnByWarehouse(Long warehouseId, String keyword, int i) {
        return itemStatusRepository.allByColumnByWarehouse(warehouseId, keyword, PageRequest.of(0, i));
    }
}
