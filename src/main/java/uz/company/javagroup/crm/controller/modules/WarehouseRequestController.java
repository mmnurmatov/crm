package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.*;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestRepository;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.modules.*;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.math.BigDecimal;
import java.util.*;

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.KOCHIRISH;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentType.NAKLADNOY;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.*;

@Controller
@RequestMapping("module/warehouse-request")
public class WarehouseRequestController extends BaseController {

    final WarehouseService warehouseService;
    final WarehouseUserService warehouseUserService;
    final WarehouseProductService warehouseProductService;
    final WarehouseProductItemService warehouseProductItemService;
    final ProductService productService;
    final WarehouseRequestRepository warehouseRequestRepository;
    final WarehouseRequestItemRepository warehouseRequestItemRepository;
    final ItemStatusService itemStatusService;

    public WarehouseRequestController(WarehouseService warehouseService, WarehouseUserService warehouseUserService, WarehouseProductService warehouseProductService, WarehouseProductItemService warehouseProductItemService, ProductService productService, WarehouseRequestRepository warehouseRequestRepository, WarehouseRequestItemRepository warehouseRequestItemRepository, ItemStatusService itemStatusService) {
        this.warehouseService = warehouseService;
        this.warehouseUserService = warehouseUserService;
        this.warehouseProductService = warehouseProductService;
        this.warehouseProductItemService = warehouseProductItemService;
        this.productService = productService;
        this.warehouseRequestRepository = warehouseRequestRepository;
        this.warehouseRequestItemRepository = warehouseRequestItemRepository;
        this.itemStatusService = itemStatusService;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseRequestRead')")
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
        List<Object[]> toWarehouseRequests = warehouseRequestRepository.ownWithSumQtyAndStatus4ToWarehouse2Status(READY, PENDING, Utils.ownWarehouseIds(this.getAuthUserId(), warehouseUserService));
        List<Object[]> fromWarehouseRequests = warehouseRequestRepository.ownWithSumQtyAndStatus4FromWarehouse(READY, WAITING, CANCELLED, PENDING, Utils.ownWarehouseIds(this.getAuthUserId(), warehouseUserService));
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("toWarehouseRequests", toWarehouseRequests);
        modelAndView.addObject("fromWarehouseRequests", fromWarehouseRequests);
        modelAndView.setViewName("/pages/modules/warehouse-requests/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ModelAndView createWarehouse(ModelAndView modelAndView) throws RecordNotFoundException {
        List<Warehouse> ownWarehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        List<Warehouse> allWarehouses = warehouseService.findAll();
        modelAndView.addObject("ownWarehouses", ownWarehouses);
        modelAndView.addObject("allWarehouses", allWarehouses);
        modelAndView.setViewName("/pages/modules/warehouse-requests/create");
        return modelAndView;
    }


    @RequestMapping(path = "/requestCheck", method = RequestMethod.POST)
    public ModelAndView requestCheck(@RequestParam("requestId") WarehouseProduct warehouseProduct,
                                     @RequestParam("whItems") List<WarehouseProductItem> warehouseProductItems,
                                     ModelAndView modelAndView){

        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.setViewName("/pages/modules/warehouse-requests/transfer-check");
        return modelAndView;
    }
    
}
