package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
import uz.isd.javagroup.grandcrm.entity.directories.ProductWrapper;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ProductService;
import uz.isd.javagroup.grandcrm.services.directories.ProductWrapperService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("directory/product-wrappers")
public class ProductWrapperController extends BaseController {

    @Autowired
    ProductService productService;
    @Autowired
    ProductWrapperService productWrapperService;

    @RequestMapping(path = {"/{productId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ProductWrapperRead')")
    public ModelAndView index(@PathVariable(value = "productId") Long parentId, @ModelAttribute("status") String status,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {

        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }
        List<ProductWrapper> productWrappers = productWrapperService.findByParent(parentId);
        List<Product> products = productService.findAll();
//        if (products == null || products.size() == 0) products = productService.findAll();
        modelAndView.addObject("productWrappers", productWrappers);
        modelAndView.addObject("products", products);
        modelAndView.addObject("parentId", parentId);
        modelAndView.setViewName("/pages/directories/product-wrappers/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductWrapperCreate')")
    public RedirectView createProductWrapper(@RequestParam(name = "productId") Long parentId,
                                             @RequestParam(name = "wrapperId") Long wrappedId,
                                             @RequestParam(name = "amount") BigDecimal amount,
                                             RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ProductWrapper productWrapper = new ProductWrapper();
        productWrapper.setParent(productService.getProductById(parentId));
        productWrapper.setWrapped(productService.getProductById(wrappedId));
        productWrapper.setAmount(amount);
        productWrapperService.saveProductWrapper(productWrapper);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/product-wrappers/" + parentId);
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductWrapperUpdate')")
    public RedirectView updateProductWrapper(@RequestParam(name = "id") Long id,
                                             @RequestParam(name = "parentIdEdit") Long parentId,
                                             @RequestParam(name = "wrapperEdit") Long wrappedId,
                                             @RequestParam(name = "amountEdit") BigDecimal amount,
                                             RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ProductWrapper productWrapper = productWrapperService.getProductWrapperById(id);
        productWrapper.setId(id);
        productWrapper.setParent(productService.getProductById(parentId));
        productWrapper.setWrapped(productService.getProductById(wrappedId));
        productWrapper.setAmount(amount);
        productWrapperService.saveProductWrapper(productWrapper);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/product-wrappers/" + parentId);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductWrapperDelete')")
    public RedirectView deleteProduct(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        productWrapperService.deleteProductWrapper(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/product-wrappers/");
    }
}
