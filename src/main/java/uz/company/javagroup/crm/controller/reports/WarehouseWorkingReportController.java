package uz.isd.javagroup.grandcrm.controller.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseProductService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;

import java.util.List;


@Controller
@RequestMapping("report/warehouse-working-reports")
public class WarehouseWorkingReportController extends BaseController {

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    WarehouseProductService warehouseProductService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseWorkingReportRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Warehouse> warehouses = warehouseService.findAll();
        modelAndView.addObject("warehouses", warehouses);

        modelAndView.setViewName("/pages/reports/warehouse-working-reports/index");
        return modelAndView;
    }

}
