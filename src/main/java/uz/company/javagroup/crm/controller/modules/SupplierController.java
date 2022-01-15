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
import uz.isd.javagroup.grandcrm.entity.modules.Supplier;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.SupplierService;
import uz.isd.javagroup.grandcrm.services.modules.UserService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("module/supplier")
public class SupplierController  extends BaseController {

    @Autowired
    SupplierService supplierService;

    @Autowired
    UserService userService;


    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SupplierRead')")
    public ModelAndView index(ModelAndView modelAndView,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Supplier> supplierList = supplierService.findAll();
        List<User> users = userService.findAll();
        modelAndView.setViewName("/pages/modules/suppliers/index");
        modelAndView.addObject("supplierList", supplierList);
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SupplierCreate')")
    public RedirectView createCash(@RequestParam(name = "userId") Long userId,
                                   @RequestParam(name = "status") int status,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Supplier supplier = new Supplier();

        supplier.setStatus(status);
        supplier.setDebet(BigDecimal.ZERO);
        supplier.setKredit(BigDecimal.ZERO);
        supplier.setSaldo(BigDecimal.ZERO);
        supplier.setLastUpdate(new Date());
        if (userId != 0) {
            supplier.setUser(userService.getUserById(userId));
        }

        supplierService.saveSupplier(supplier);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/supplier/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SupplierUpdate')")
    public RedirectView updateCash(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "userEditId") Long userId,
                                   @RequestParam(name = "statusEdit") int status,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        Supplier supplier = supplierService.getSupplierById(id);
        supplier.setId(id);
        supplier.setStatus(status);
        if (userId != 0) {
            supplier.setUser(userService.getUserById(userId));
        }

        supplierService.saveSupplier(supplier);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/supplier/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SupplierDelete')")
    public RedirectView deleteCash(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        supplierService.deleteSupplier(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/supplier/");
    }

}
