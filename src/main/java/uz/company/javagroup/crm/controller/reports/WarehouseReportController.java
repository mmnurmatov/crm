package uz.isd.javagroup.grandcrm.controller.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseUserService;
import uz.isd.javagroup.grandcrm.utility.Utils;

import java.util.List;

@Controller
@RequestMapping("warehouse-report/report")
public class WarehouseReportController extends BaseController {

    @Autowired
    WarehouseService warehouseService;
    @Autowired
    WarehouseUserService warehouseUserService;

    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("status") String status,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {

        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }
        List<Warehouse> ownWarehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        modelAndView.addObject("ownWarehouses", ownWarehouses);
        modelAndView.setViewName("/pages/reports/warehouse-reports/index");
        return modelAndView;
    }


}
