package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseProductItem;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.WarehouseProductItemRepository;
import uz.isd.javagroup.grandcrm.services.modules.ItemStatusService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductItemService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("module/warehouse-product-pending-item")
public class WarehouseProductPendingController extends BaseController {

    @Autowired
    WarehouseProductItemService warehouseProductItemService;
    @Autowired
    WarehouseProductItemRepository warehouseProductItemRepository;
    @Autowired
    WarehouseProductService warehouseProductService;

    @Autowired
    ItemStatusService itemStatusService;

    @RequestMapping(path = "/{warehouseProductId}", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('WarehouseProductWarehouseProductRecieveItemRead')")
    public ModelAndView pendingToRecieveAll(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {
        if (!warehouseProductId.isPresent()) {
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

        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemRepository.findAllByWarehouseProductIdAndRemainingIsGreaterThan(warehouseProductId.get(), BigDecimal.ZERO);
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        WarehouseProduct.Action action = warehouseProduct.getAction();

        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.addObject("warehouseProductAction", action.name());
        modelAndView.setViewName("/pages/modules/warehouse-product-pending-items/index");
        return modelAndView;
    }


    @RequestMapping(path = "/parcial/{warehouseProductId}", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('WarehouseProductWarehouseProductRecieveItemRead')")
    public ModelAndView pendingToRecieveParcial(@PathVariable(name = "warehouseProductId") Optional<Long> warehouseProductId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {
        if (!warehouseProductId.isPresent()) {
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

        List<WarehouseProductItem> warehouseProductItems = warehouseProductItemService.findAllNotZeroByWarehouseProductId(warehouseProductId.get());
        WarehouseProduct warehouseProduct = warehouseProductService.getWarehouseProductById(warehouseProductId.get());

        WarehouseProduct.Action action = warehouseProduct.getAction();

        modelAndView.addObject("warehouseProduct", warehouseProduct);
        modelAndView.addObject("warehouseProductItems", warehouseProductItems);
        modelAndView.addObject("warehouseProductAction", action.name());
        modelAndView.setViewName("/pages/modules/warehouse-product-pending-items/parcial");
        return modelAndView;
    }
}
