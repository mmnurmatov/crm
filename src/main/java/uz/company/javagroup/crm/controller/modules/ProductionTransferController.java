package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseUserService;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.util.List;

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.KIRIM;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.KOCHIRISH;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentStatus.PENDING;
import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.DocumentStatus.READY;

@Controller
@RequestMapping("module/production-product-transfer")
public class ProductionTransferController extends BaseController {

    @Autowired
    WarehouseProductService warehouseProductService;
    @Autowired
    WarehouseUserService warehouseUserService;
    @Autowired
    WarehouseService warehouseService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('productionProductTransferRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) throws RecordNotFoundException {

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

        List<Warehouse> ownWarehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        List<Warehouse> allWarehouses = warehouseService.findAll();
        List<Object[]> warehouseProducts = warehouseProductService.ownWithSummaAndQtyAndActionFromWarehouseProd(KOCHIRISH, ownWarehouseIds);
        List<Object[]> warehouseProductTransferReceive = warehouseProductService.ownWithSummaAndQtyAndStatusAndActionToWarehouseProd(READY, KIRIM, ownWarehouseIds);
        List<Object[]> warehouseProductTransferPending = warehouseProductService.ownWithSummaAndQtyAndStatusAndActionToWarehouseProd(PENDING, KIRIM, ownWarehouseIds);
        modelAndView.addObject("ownWarehouses", ownWarehouses);
        modelAndView.addObject("allWarehouses", allWarehouses);
        modelAndView.addObject("warehouseProducts", warehouseProducts);
        modelAndView.addObject("warehouseProductTransferReceive", warehouseProductTransferReceive);
        modelAndView.addObject("warehouseProductTransferPending", warehouseProductTransferPending);
        modelAndView.setViewName("/pages/modules/production-transfers/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('WarehouseProductCreate')")
    public ModelAndView createWarehouseProductTransfer(ModelAndView modelAndView) throws RecordNotFoundException {
        User user = this.getAuthUserData();
        List<Warehouse> ownProductions;
        if (user.getRole().getCode().equals("DIRECTOR") || user.getRole().getCode().equals("SYS_ADMIN") || user.getRole().getCode().equals("ACCOUNTANT") || user.getRole().getCode().equals("ADMIN"))
            ownProductions = warehouseService.findAllProductions();
        else ownProductions = Utils.ownProductions(this.getAuthUserId(), warehouseUserService);
        modelAndView.addObject("ownProductions", ownProductions);
        modelAndView.setViewName("/pages/modules/production-transfers/create");
        return modelAndView;
    }
}
