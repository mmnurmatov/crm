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
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.entity.modules.WarehouseUser;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.UserService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseUserService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("module/warehouse-user")
public class WarehouseUserController extends BaseController {

    @Autowired
    WarehouseUserService warehouseUserService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseUserRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<WarehouseUser> warehouseUsers = warehouseUserService.findAllWarehouseUsers();

        List<Warehouse> warehouses = warehouseService.findAll();
        List<User> users = userService.findAll();

        modelAndView.addObject("warehouseUsers", warehouseUsers);
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("users", users);

        modelAndView.setViewName("/pages/modules/warehouse-users/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseUserCreate')")
    public RedirectView createWarehouseUser(@RequestParam(name = "warehouse") Long warehouseId,
                                            @RequestParam(name = "userCreateId") Long userId,
                                            @RequestParam(name = "status") Optional<Integer> status,
                                            RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        WarehouseUser warehouseUser = new WarehouseUser();
        if (warehouseId != 0) warehouseUser.setWarehouse(warehouseService.getWarehouseById(warehouseId));
        if (userId != 0) warehouseUser.setUser(userService.getUserById(userId));
        if (status.isPresent())  warehouseUser.setStatus(status.get());
        warehouseUser.setCreatedAt(new Date());
        warehouseUserService.saveWarehouseUser(warehouseUser);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");

        return new RedirectView("/module/warehouse-user/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseUserUpdate')")
    public RedirectView updateWarehouseUser(@RequestParam(name = "id") Long id,
                                            @RequestParam(name = "warehouseId") Long warehouseId,
                                            @RequestParam(name = "userId") Long userId,
                                            @RequestParam(name = "statusEdit") Optional<Integer> status,
                                            RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        WarehouseUser warehouseUser = warehouseUserService.getWarehouseUserById(id);
        if (warehouseId != 0) warehouseUser.setWarehouse(warehouseService.getWarehouseById(warehouseId));
        if (userId != 0) warehouseUser.setUser(userService.getUserById(userId));
        if (status.isPresent()) warehouseUser.setStatus(status.get());
        warehouseUser.setCreatedAt(new Date());
        warehouseUserService.saveWarehouseUser(warehouseUser);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/warehouse-user/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseUserDelete')")
    public RedirectView deleteWarehouseUser(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        warehouseUserService.deleteWarehouseUser(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/warehouse-user/");
    }
}
