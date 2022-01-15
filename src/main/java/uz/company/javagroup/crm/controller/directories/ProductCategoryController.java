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
import uz.isd.javagroup.grandcrm.entity.directories.ProductCategory;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ProductCategoryService;

import java.util.List;

@Controller
@RequestMapping("directory/product-categories")
public class ProductCategoryController extends BaseController {

    @Autowired
    ProductCategoryService productCategoryService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ProductCategoryRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String status, @ModelAttribute("message") String messageData) {

        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }

        List<ProductCategory> productCategories = productCategoryService.findAll();
        modelAndView.addObject("productCategories", productCategories);
        modelAndView.setViewName("/pages/directories/product-categories/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductCategoryCreate')")
    public RedirectView createProductCategory(
                                          @RequestParam(name = "nameUz") String nameUz,
                                          @RequestParam(name = "nameRu") String nameRu,
                                          @RequestParam(name = "nameEn") String nameEn,
                                          @RequestParam(name = "iconClass") String iconClass,
                                          RedirectAttributes redirectAttributes) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setNameRu(nameRu);
        productCategory.setNameUz(nameUz);
        productCategory.setNameEn(nameEn);
        productCategory.setIconClass(iconClass);
        productCategoryService.saveProductCategory(productCategory);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/product-categories/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductCategoryUpdate')")
    public RedirectView updateProductCategory(@RequestParam(name = "id") Long id,
                                          @RequestParam(name = "nameUzEdit") String nameUz,
                                          @RequestParam(name = "nameRuEdit") String nameRu,
                                          @RequestParam(name = "nameEnEdit") String nameEn,
                                          @RequestParam(name = "iconClassEdit") String iconClass,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ProductCategory productCategory = productCategoryService.getProductCategoryById(id);
        productCategory.setId(id);
        productCategory.setNameUz(nameUz);
        productCategory.setNameRu(nameRu);
        productCategory.setNameEn(nameEn);
        productCategory.setIconClass(iconClass);
        productCategoryService.saveProductCategory(productCategory);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/product-categories/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductCategoryDelete')")
    public RedirectView deleteProductCategory(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        productCategoryService.deleteProductCategory(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/product-categories/");
    }
}
