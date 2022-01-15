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
import uz.isd.javagroup.grandcrm.entity.directories.ProductUnit;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ProductUnitService;

import java.util.List;

@Controller
@RequestMapping("directory/product-units")
public class ProductUnitController extends BaseController {

    @Autowired
    ProductUnitService productUnitService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ProductUnitRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String status, @ModelAttribute("message") String messageData) {

        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }

        List<ProductUnit> productUnits = productUnitService.findAll();
        modelAndView.setViewName("/pages/directories/product-units/index");
        modelAndView.addObject("productUnits", productUnits);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductUnitCreate')")
    public RedirectView createProductUnit(@RequestParam(name = "nameUz") String nameUz,
                                          @RequestParam(name = "nameRu") String nameRu,
                                          @RequestParam(name = "nameEn") String nameEn,
                                          @RequestParam(name = "code") String code,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ProductUnit productUnit = new ProductUnit();
        productUnit.setNameRu(nameRu);
        productUnit.setNameUz(nameUz);
        productUnit.setNameEn(nameEn);
        productUnit.setCode(code);
        productUnitService.saveProductUnit(productUnit);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/product-units/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductUnitUpdate')")
    public RedirectView updateProductUnit(@RequestParam(name = "id") Long id,
                                          @RequestParam(name = "nameUzEdit") String nameUz,
                                          @RequestParam(name = "nameRuEdit") String nameRu,
                                          @RequestParam(name = "nameEnEdit") String nameEn,
                                          @RequestParam(name = "codeEdit") String code,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ProductUnit productUnit = productUnitService.getProductUnitById(id);
        productUnit.setId(id);
        productUnit.setNameUz(nameUz);
        productUnit.setNameRu(nameRu);
        productUnit.setNameEn(nameEn);
        productUnit.setCode(code);
        productUnitService.saveProductUnit(productUnit);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/product-units/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductUnitDelete')")
    public RedirectView deleteProductUnit(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        productUnitService.deleteProductUnit(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/product-units/");
    }
}
