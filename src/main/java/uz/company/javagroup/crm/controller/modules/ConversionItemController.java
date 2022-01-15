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
import uz.isd.javagroup.grandcrm.entity.modules.Conversion;
import uz.isd.javagroup.grandcrm.entity.modules.ConversionItem;
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
@RequestMapping("module/conversion-item")
public class ConversionItemController extends BaseController {

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

    @RequestMapping(path = "/{conversionId}", method = RequestMethod.GET)
    public ModelAndView index(@PathVariable Long conversionId, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<ConversionItem> conversionItems = conversionItemService.findAllByConversionId(conversionId);
        Conversion conversion = conversionService.getConversionById(conversionId);

        modelAndView.addObject("conversionItems", conversionItems);
        modelAndView.addObject("conversion", conversion);
        modelAndView.setViewName("/pages/modules/conversion-items/index");
        return modelAndView;
    }
}
