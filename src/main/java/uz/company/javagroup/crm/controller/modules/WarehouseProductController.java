package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.entity.directories.ProductCategory;
import uz.isd.javagroup.grandcrm.entity.directories.ProductType;
import uz.isd.javagroup.grandcrm.entity.directories.ProductUnit;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestRepository;
import uz.isd.javagroup.grandcrm.services.directories.*;
import uz.isd.javagroup.grandcrm.services.modules.*;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.*;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentStatus.PENDING;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentStatus.READY;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.CANCELLED;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.WAITING;

@Controller
@RequestMapping("module/warehouse-product")
public class WarehouseProductController extends BaseController {
    @Autowired
    WarehouseProductService warehouseProductService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    WarehouseUserService warehouseUserService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    ProductUnitService productUnitService;
    @Autowired
    ProductTypeService productTypeService;
    @Autowired
    WarehouseProductItemService warehouseProductItemService;
    @Autowired
    ItemStatusService itemStatusService;
    @Autowired
    WarehouseRequestRepository warehouseRequestRepository;
    @Autowired
    ContragentService contragentService;

    @Autowired
    WarehouseRequestItemRepository warehouseRequestItemRepository;

    @Autowired
    CountryService countryService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseProductRead')")
    public ModelAndView index(ModelAndView modelAndView,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Warehouse> warehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        List<Object[]> warehouseProducts = warehouseProductService.ownWithSummaAndQtyAndActionToWarehouse(KIRIM, Utils.ownWarehouseIds(this.getAuthUserId(), warehouseUserService));
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("warehouseProducts", warehouseProducts);
        modelAndView.setViewName("/pages/modules/warehouse-products/index");
        return modelAndView;
    }

    @RequestMapping(path = "/waitingOrCancelled", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseConfirmationRead')")
    public ModelAndView waiting(ModelAndView modelAndView,
                                @ModelAttribute("status") String typesData,
                                @ModelAttribute("message") String messageData) throws RecordNotFoundException {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        User user = this.getAuthUserData();
        List<Long> ownWarehouseIds;
        if (user.getRole().getCode().equals("DIRECTOR") || user.getRole().getCode().equals("SYS_ADMIN") || user.getRole().getCode().equals("ACCOUNTANT") || user.getRole().getCode().equals("ADMIN"))
            ownWarehouseIds = warehouseUserService.warehouseIds();
        else ownWarehouseIds = Utils.ownWarehouseIds(this.getAuthUserId(), warehouseUserService);
        List<Object[]> warehouseProductsTransfer = warehouseProductService.ownWithSummaAndQtyAndStatusAndActionToWarehouseNotLinked(WarehouseProduct.DocumentStatus.WAITING, WarehouseProduct.DocumentStatus.CANCELLED, KOCHIRISH, ownWarehouseIds);
        List<Object[]> warehouseProductsExpense = warehouseProductService.ownWithSummaAndQtyAndStatusAndActionFromWarehouseOrToWarehouseNotLinked(WarehouseProduct.DocumentStatus.WAITING, WarehouseProduct.DocumentStatus.CANCELLED, CHIQIM, ownWarehouseIds);
        List<Object[]> requests = warehouseRequestRepository.ownWithSumQtyAndStatusFromWarehouse(WAITING, CANCELLED, ownWarehouseIds);
        modelAndView.addObject("requests", requests);
        modelAndView.addObject("warehouseProductsTransfer", warehouseProductsTransfer);
        modelAndView.addObject("warehouseProductsExpense", warehouseProductsExpense);
        modelAndView.setViewName("/pages/modules/invoice/accept");
        return modelAndView;
    }

    @RequestMapping(path = "/transferWaitingToReadyAll/{warehouseProductId}", method = RequestMethod.GET)
    @Transactional
    public RedirectView transferWaitingToReadyAll(@PathVariable("warehouseProductId") Long warehouseProductId,
                                                  RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProduct.setAction(KOCHIRISH);
        warehouseProduct.setDocumentStatus(READY);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
        List<WarehouseProductItem> items = warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId);
        warehouseProduct.setWhItems(items);
        itemStatusService.countTransferWhDoc(warehouseProduct);

        WarehouseProduct warehouseProductK = new WarehouseProduct();
        warehouseProductK.setCreatedAt(new Date());
        warehouseProductK.setAction(KIRIM);
        warehouseProductK.setDocumentType(warehouseProduct.getDocumentType());
        warehouseProductK.setFromWarehouse(warehouseProduct.getFromWarehouse());
        warehouseProductK.setToWarehouse(warehouseProduct.getToWarehouse());
        warehouseProductK.setDocumentStatus(PENDING);
        warehouseProductK.setLinkedWarehouse(warehouseProduct);
        Utils.generateRegNumber(warehouseProductK, warehouseProductService);
        warehouseProductService.saveWarehouseProduct(warehouseProductK);
        if (items != null && items.size() > 0) {
            for (WarehouseProductItem i : items) {
                WarehouseProductItem whDocumentItem = new WarehouseProductItem();
                whDocumentItem.setWarehouseProduct(warehouseProductK);
                whDocumentItem.setProduct(i.getProduct());
                whDocumentItem.setIncomePrice(i.getIncomePrice());
                whDocumentItem.setCount(i.getCount());
                whDocumentItem.setRemaining(i.getCount());
                whDocumentItem.setCreatedAt(new Date());
                warehouseProductItemService.saveWarehouseProductItem(whDocumentItem);
            }
        }

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Amaliyot muvvaffaqiyatli bajarildi!");
        return new RedirectView("/module/warehouse-product/waitingOrCancelled");
    }

    @RequestMapping(path = "/warehouseProductExpenseWaitingToReady/{warehouseProductId}", method = RequestMethod.GET)
    @Transactional
    public RedirectView createWarehouseProductExpenseWaitingToReady(@PathVariable("warehouseProductId") Long warehouseProductId,
                                                                    RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProduct.setAction(CHIQIM);
        warehouseProduct.setDocumentStatus(READY);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);
//        warehouseProduct.setWhItems(warehouseProductItemService.findAllByWarehouseProductId(warehouseProductId));
        itemStatusService.countExpense(warehouseProduct);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Amaliyot muvvaffaqiyatli bajarildi!");
        return new RedirectView("/module/warehouse-product/waitingOrCancelled");
    }


    @RequestMapping(path = "/expenseDelete/{warehouseProductId}", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('WarehouseProductCreate')")
    @Transactional
    public RedirectView expenseDelete(@PathVariable("warehouseProductId") Long warehouseProductId,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProduct.setReason("");
        warehouseProduct.setAction(CHIQIM);
        warehouseProduct.setDocumentStatus(WarehouseProduct.DocumentStatus.CANCELLED);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli o'chirildi");
        return new RedirectView("/module/warehouse-product/waitingOrCancelled");
    }


    @RequestMapping(path = "/transferDelete/{warehouseProductId}", method = RequestMethod.GET)
    @Transactional
    public RedirectView transferDelete(@PathVariable("warehouseProductId") Long warehouseProductId,
                                       RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId);
        warehouseProduct.setCreatedAt(new Date());
        warehouseProduct.setReason("");
        warehouseProduct.setAction(KOCHIRISH);
        warehouseProduct.setDocumentStatus(WarehouseProduct.DocumentStatus.CANCELLED);
        warehouseProductService.saveWarehouseProduct(warehouseProduct);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli bekor qilindi!");
        return new RedirectView("/module/warehouse-product/waitingOrCancelled");
    }

    // for director cancel request
    @RequestMapping(path = "/cancel/{warehouseRequestId}", method = RequestMethod.GET)
    public RedirectView cancelRequest(@PathVariable Long warehouseRequestId,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        Optional<WarehouseRequest> warehouseRequest = warehouseRequestRepository.findById(warehouseRequestId);
        if (!warehouseRequest.isPresent())
            throw new RecordNotFoundException("warehouseRequest is not found id: " + warehouseRequestId);
        warehouseRequest.get().setDocumentStatus(CANCELLED);
        warehouseRequestRepository.save(warehouseRequest.get());

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli bekor qilindi!");
        return new RedirectView("/module/warehouse-product/waitingOrCancelled");
    }


    @RequestMapping(path = "warehouseRequestItem/{warehouseRequestId}", method = RequestMethod.GET)
    public ModelAndView index(@PathVariable(name = "warehouseRequestId") Optional<Long> warehouseRequestId, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {

        if (!warehouseRequestId.isPresent()) {
            modelAndView.addObject("status", false);
            modelAndView.addObject("message", "Invalid warehouseProductId...");
        }
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<WarehouseRequestItem> warehouseRequestItems = warehouseRequestItemRepository.findAllByWarehouseRequestId(warehouseRequestId.get());
        Optional<WarehouseRequest> warehouseRequest = warehouseRequestRepository.findById(warehouseRequestId.get());

        modelAndView.addObject("warehouseRequestItems", warehouseRequestItems);
        modelAndView.addObject("warehouseRequest", warehouseRequest.get());
        modelAndView.setViewName("/pages/modules/invoice/warehouse-request-items");
        return modelAndView;
    }


    // for director cancel request
    @RequestMapping(path = "/pending/{warehouseRequestId}", method = RequestMethod.GET)
    public RedirectView pendingRequest(@PathVariable Long warehouseRequestId,
                                       RedirectAttributes redirectAttributes) throws RecordNotFoundException {


        Optional<WarehouseRequest> warehouseRequest = warehouseRequestRepository.findById(warehouseRequestId);
        if (!warehouseRequest.isPresent())
            throw new RecordNotFoundException("warehouseRequest is not found id: " + warehouseRequestId);
        warehouseRequest.get().setDocumentStatus(WarehouseRequest.DocumentStatus.PENDING);
        warehouseRequestRepository.save(warehouseRequest.get());

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Amaliyot muvvaffaqiyatli bajarildi!");
        return new RedirectView("/module/warehouse-product/waitingOrCancelled");
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseProductCreate')")
    public ModelAndView createWarehouseProduct(ModelAndView modelAndView,
                                               @ModelAttribute("status") String typesData,
                                               @ModelAttribute("message") String messageData) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Warehouse> warehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        List<ProductCategory> productCategories = productCategoryService.findAll();
        List<ProductType> productTypes = productTypeService.findAll();
        List<ProductUnit> productUnits = productUnitService.findAll();
        List<Contragent> contragents = contragentService.findAll();
        List<Country> countries = countryService.findAll();

        modelAndView.addObject("contragents", contragents);
        modelAndView.addObject("countries", countries);
        modelAndView.addObject("productCategories", productCategories);
        modelAndView.addObject("productTypes", productTypes);
        modelAndView.addObject("productUnits", productUnits);
        modelAndView.addObject("warehouses", warehouses);

        modelAndView.setViewName("/pages/modules/warehouse-products/create");
        return modelAndView;
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseProductUpdate')")
    public RedirectView updateWarehouseProduct(@RequestParam(name = "contragent_id") Long contragentId,
                                               @RequestParam(name = "id") Long id,
                                               @RequestParam(name = "warehouseId") Long warehouseId,
                                               @RequestParam(name = "summa") BigDecimal summa,
                                               RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(id);
        if (warehouseId != 0) warehouseProduct.setToWarehouse(warehouseService.getWarehouseById(warehouseId));
        if (contragentId != null && contragentId != 0)
            warehouseProduct.setContragent(contragentService.getContragentById(contragentId));
        warehouseProduct.setSumma(summa);
        warehouseProduct.setUpdatedAt(new Date());
        warehouseProductService.saveWarehouseProduct(warehouseProduct);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");

        return new RedirectView("/module/warehouse-product/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseProductDelete')")
    public RedirectView deleteWarehouseProduct(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        warehouseProductService.deleteWarehouseProduct(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/warehouse-product/");
    }
}
