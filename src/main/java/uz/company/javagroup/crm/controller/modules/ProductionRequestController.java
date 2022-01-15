package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestItemRepository;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseRequestRepository;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.modules.*;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.util.List;

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseRequest.DocumentStatus.*;

@Controller
@RequestMapping("module/production-request")
public class ProductionRequestController extends BaseController {

    final WarehouseService warehouseService;
    final WarehouseUserService warehouseUserService;
    final WarehouseProductService warehouseProductService;
    final WarehouseProductItemService warehouseProductItemService;
    final ProductService productService;
    final WarehouseRequestRepository warehouseRequestRepository;
    final WarehouseRequestItemRepository warehouseRequestItemRepository;
    final ItemStatusService itemStatusService;

    public ProductionRequestController(WarehouseService warehouseService, WarehouseUserService warehouseUserService, WarehouseProductService warehouseProductService, WarehouseProductItemService warehouseProductItemService, ProductService productService, WarehouseRequestRepository warehouseRequestRepository, WarehouseRequestItemRepository warehouseRequestItemRepository, ItemStatusService itemStatusService) {
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
    @PreAuthorize("hasAuthority('ProductionRequestRead')")
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
        User user = this.getAuthUserData();
        List<Warehouse> warehouses;
        List<Long> ownWarehouseIds;
        if (user.getRole().getCode().equals("DIRECTOR") || user.getRole().getCode().equals("SYS_ADMIN") || user.getRole().getCode().equals("ACCOUNTANT") || user.getRole().getCode().equals("ADMIN")) {
            warehouses = warehouseService.findAllProductions();
            ownWarehouseIds = warehouseUserService.warehouseIds();
        } else {
            warehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
            ownWarehouseIds = Utils.ownWarehouseIds(this.getAuthUserId(), warehouseUserService);
        }
        List<Object[]> toWarehouseRequests = warehouseRequestRepository.ownWithSumQtyAndStatus4ToWarehouse2StatusProd(READY, PENDING, ownWarehouseIds);
        List<Object[]> fromWarehouseRequests = warehouseRequestRepository.ownWithSumQtyAndStatus4FromWarehouseProd(READY, WAITING, CANCELLED, PENDING, ownWarehouseIds);
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("toWarehouseRequests", toWarehouseRequests);
        modelAndView.addObject("fromWarehouseRequests", fromWarehouseRequests);
        modelAndView.setViewName("/pages/modules/production-requests/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public ModelAndView createWarehouse(ModelAndView modelAndView) throws RecordNotFoundException {
        User user = this.getAuthUserData();
        List<Warehouse> ownProductions;
        if (user.getRole().getCode().equals("DIRECTOR") || user.getRole().getCode().equals("SYS_ADMIN") || user.getRole().getCode().equals("ACCOUNTANT") || user.getRole().getCode().equals("ADMIN"))
            ownProductions = warehouseService.findAllProductions();
        else ownProductions = Utils.ownProductions(this.getAuthUserId(), warehouseUserService);
        modelAndView.addObject("ownProductions", ownProductions);
        modelAndView.setViewName("/pages/modules/production-requests/create");
        return modelAndView;
    }

    @RequestMapping(path = "/requestCheck", method = RequestMethod.POST)
    public ModelAndView requestCheck(@RequestParam("requestId") WarehouseProduct warehouseProduct,
                                     @RequestParam("whItems") List<WarehouseProductItem> warehouseProductItems,
                                     ModelAndView modelAndView) {
        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.setViewName("/pages/modules/production-requests/transfer-check");
        return modelAndView;
    }
}
