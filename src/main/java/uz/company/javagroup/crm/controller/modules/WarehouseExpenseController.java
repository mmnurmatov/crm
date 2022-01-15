package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.entity.modules.Contragent;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.CountryService;
import uz.isd.javagroup.grandcrm.services.modules.*;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.util.List;

import static uz.isd.javagroup.grandcrm.entity.modules.WarehouseProduct.Action.CHIQIM;

@Controller
@RequestMapping("module/warehouse-expense")
public class WarehouseExpenseController extends BaseController {
    @Autowired
    WarehouseExpenseService warehouseExpenseService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    WarehouseUserService warehouseUserService;
    @Autowired
    WarehouseProductService warehouseProductService;
    @Autowired
    ContragentService contragentService;

    @Autowired
    CountryService countryService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseExpenseRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Warehouse> warehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        List<Object[]> warehouseProducts = warehouseProductService.ownWithSummaAndQtyAndActionFromWarehouse(CHIQIM, Utils.ownWarehouseIds(this.getAuthUserId(), warehouseUserService));
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("warehouseProducts", warehouseProducts);
        modelAndView.setViewName("/pages/modules/warehouse-expenses/index");
        return modelAndView;
    }


    @RequestMapping(path = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseExpenseRead')")
    public ModelAndView newExpenseView(ModelAndView modelAndView,
                                       @ModelAttribute("status") String typesData,
                                       @ModelAttribute("message") String messageData) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Contragent> contragents = contragentService.findAll();
        List<Warehouse> warehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        List<Object[]> warehouseProducts = warehouseProductService.ownWithSummaAndQtyAndActionFromWarehouse(CHIQIM, Utils.ownWarehouseIds(this.getAuthUserId(), warehouseUserService));
        List<Country> countries = countryService.findAll();

        modelAndView.addObject("countries", countries);
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("warehouseProducts", warehouseProducts);
        modelAndView.addObject("contragents", contragents);

        modelAndView.setViewName("/pages/modules/warehouse-expenses/create");
        return modelAndView;
    }
}
