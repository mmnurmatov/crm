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
import uz.isd.javagroup.grandcrm.entity.directories.ProductCategory;
import uz.isd.javagroup.grandcrm.entity.directories.ProductType;
import uz.isd.javagroup.grandcrm.entity.directories.ProductUnit;
import uz.isd.javagroup.grandcrm.entity.modules.Conversion;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ProductCategoryService;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.directories.ProductTypeService;
import uz.isd.javagroup.grandcrm.services.directories.ProductUnitService;
import uz.isd.javagroup.grandcrm.services.modules.ConversionItemService;
import uz.isd.javagroup.grandcrm.services.modules.ConversionService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;

import java.util.List;

@Controller
@RequestMapping("module/conversion")
public class ConversionController extends BaseController {

    @Autowired
    ConversionService conversionService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    ConversionItemService conversionItemService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductCategoryService productCategoryService;
    @Autowired
    ProductUnitService productUnitService;
    @Autowired
    ProductTypeService productTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ConversionRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<Conversion> conversions = conversionService.findAll();
        modelAndView.addObject("conversions", conversions);
        modelAndView.setViewName("/pages/modules/conversions/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ConversionCreate')")
    public ModelAndView createConversion(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) throws RecordNotFoundException {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<ProductCategory> productCategories = productCategoryService.findAll();
        List<ProductType> productTypes = productTypeService.findAll();
        List<ProductUnit> productUnits = productUnitService.findAll();
        List<Warehouse> productions = warehouseService.findAllProductions();
        modelAndView.addObject("productCategories", productCategories);
        modelAndView.addObject("productTypes", productTypes);
        modelAndView.addObject("productUnits", productUnits);
        modelAndView.addObject("productions", productions);
        modelAndView.setViewName("/pages/modules/conversions/create");
        return modelAndView;
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ConversionUpdate')")
    public RedirectView updateConversion(@RequestParam(name = "id") Long id, @RequestParam(name = "production_id") Long productionId, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Conversion conversion = conversionService.getConversionById(id);
        conversion.setProduction(warehouseService.getWarehouseById(productionId));
        conversionService.saveConversion(conversion);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/conversion/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ConversionDelete')")
    public RedirectView deleteCompany(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        conversionService.deleteConversion(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/conversion/");
    }
}
