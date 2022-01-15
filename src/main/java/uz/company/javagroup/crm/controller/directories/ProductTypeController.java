package uz.isd.javagroup.grandcrm.controller.directories;

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
import uz.isd.javagroup.grandcrm.entity.directories.ProductType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ProductTypeService;

import java.util.List;

@Controller
@RequestMapping("directory/product-types")
public class ProductTypeController extends BaseController {

    @Autowired
    ProductTypeService productTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ProductTypeRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String status, @ModelAttribute("message") String messageData) {

        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }

        List<ProductType> productTypes = productTypeService.findAll();
        modelAndView.setViewName("/pages/directories/product-types/index");
        modelAndView.addObject("productTypes", productTypes);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductTypeCreate')")
    public RedirectView createProductType(@RequestParam(name = "nameUz") String nameUz,
                                          @RequestParam(name = "nameRu") String nameRu,
                                          @RequestParam(name = "nameEn") String nameEn,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ProductType productType = new ProductType();
        productType.setNameRu(nameRu);
        productType.setNameUz(nameUz);
        productType.setNameEn(nameEn);
        productTypeService.saveProductType(productType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/product-types/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductTypeUpdate')")
    public RedirectView updateProductType(@RequestParam(name = "id") Long id,
                                          @RequestParam(name = "nameUzEdit") String nameUz,
                                          @RequestParam(name = "nameRuEdit") String nameRu,
                                          @RequestParam(name = "nameEnEdit") String nameEn,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ProductType productType = productTypeService.getProductTypeById(id);
        productType.setId(id);
        productType.setNameUz(nameUz);
        productType.setNameRu(nameRu);
        productType.setNameEn(nameEn);
        productTypeService.saveProductType(productType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/product-types/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductTypeDelete')")
    public RedirectView deleteProductType(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        productTypeService.deleteProductType(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/product-types/");
    }
}
