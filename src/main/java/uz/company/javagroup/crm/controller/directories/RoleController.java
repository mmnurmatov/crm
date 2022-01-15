package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationData;
import uz.isd.javagroup.grandcrm.entity.directories.Permission;
import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.*;
import uz.isd.javagroup.grandcrm.services.modules.UserService;
import uz.isd.javagroup.grandcrm.utility.NotificationSending;

import java.util.*;

@Controller
@RequestMapping("directory/roles")
public class RoleController extends BaseController {

    @Autowired
    RoleService roleService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserService userService;

    @Autowired
    NotificationDataService notificationDataService;

    @Autowired
    NotificationTemplateService notificationTemplateService;

    @Autowired
    NotificationTypeService notificationTypeService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationUserService notificationUserService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('RoleRead')")
    public ModelAndView index(ModelAndView modelAndView,
                              @ModelAttribute("status") String statusData,
                              @ModelAttribute("message") String messageData) {

        if (statusData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", statusData);
            modelAndView.addObject("message", messageData);
        }

        List<Role> roles = roleService.findAll();
        modelAndView.setViewName("/pages/directories/roles/index");
        modelAndView.addObject("roles", roles);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('RoleCreate')")
    public RedirectView createCompanyStatus(@RequestParam(name = "nameUz") String nameUz,
                                            @RequestParam(name = "nameRu") String nameRu,
                                            @RequestParam(name = "nameEn") String nameEn,
                                            @RequestParam(name = "code") String code,
                                            @RequestParam(name = "description") String description,
                                            RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Role role = new Role();
        role.setNameRu(nameRu);
        role.setNameUz(nameUz);
        role.setNameEn(nameEn);
        role.setCode(code);
        role.setDescription(description);
        roleService.saveRole(role);

        User user = userService.getUserById(this.getAuthUserId());
        Notification notification = new Notification();
        notification.setNotificationTemplate(notificationTemplateService.getNotificationTemplateById(1L));
        notification.setNotificationType(notificationTypeService.getNotificationTypeById(1L));
        notification.setCreatedAt(new Date());
        notificationService.saveNotification(notification);

        NotificationData notificationData = new NotificationData();
        notificationData.setNotification(notification);
        notificationData.setKey("Role");
        notificationData.setValue(nameUz);
        notificationDataService.saveNotificationData(notificationData);

        NotificationSending notificationSending = new NotificationSending(notification, user);
        notificationUserService.saveNotificationUser(notificationSending.sendingNotification());


        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/roles/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('RoleUpdate')")
    public RedirectView updateCompanyStatus(@RequestParam(name = "id") Long id,
                                            @RequestParam(name = "nameUzEdit") String nameUz,
                                            @RequestParam(name = "nameRuEdit") String nameRu,
                                            @RequestParam(name = "nameEnEdit") String nameEn,
                                            @RequestParam(name = "codeEdit") String code,
                                            @RequestParam(name = "descriptionEdit") String description,
                                            RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Role role = roleService.getRoleById(id);
        role.setId(id);
        role.setNameRu(nameRu);
        role.setNameUz(nameUz);
        role.setNameEn(nameEn);
        role.setCode(code);
        role.setDescription(description);
        roleService.saveRole(role);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/roles/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('RoleDelete')")
    public RedirectView deleteRole(@RequestParam(name = "rowId") Long id,
                                   RedirectAttributes redirectAttributes) {
        roleService.deleteRole(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/roles/");
    }
}
