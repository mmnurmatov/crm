package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.ExpenseType;
import uz.isd.javagroup.grandcrm.entity.modules.MonthlyConversion;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ExpenseTypeService;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.modules.MonthlyConversionService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("module/monthly-convertion-report")
public class MonthlyConversionController extends BaseController {

    @Autowired
    ExpenseTypeService expenseTypeService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    ProductService productService;
    @Autowired
    MonthlyConversionService monthlyConversionService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('MonthlyConversionRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<MonthlyConversion> monthlyConversions = monthlyConversionService.findAll();

        modelAndView.addObject("monthlyConversions", monthlyConversions);
        modelAndView.setViewName("/pages/modules/monthly-convertion-reports/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('MonthlyConversionCreate')")
    public ModelAndView createMonthlyConversion(ModelAndView modelAndView){

        List<ExpenseType> expenseTypes1 = expenseTypeService.findAllByType(1);
        List<ExpenseType> expenseTypes2 = expenseTypeService.findAllByType(2);
        List<ExpenseType> expenseTypes3 = expenseTypeService.findAllByType(3);
        List<Warehouse> productions = warehouseService.findAllProductions();

        modelAndView.addObject("productions", productions);
        modelAndView.addObject("expenseTypes1", expenseTypes1);
        modelAndView.addObject("expenseTypes2", expenseTypes2);
        modelAndView.addObject("expenseTypes3", expenseTypes3);

        modelAndView.setViewName("/pages/modules/monthly-convertion-reports/create");
        return modelAndView;
    }
}
