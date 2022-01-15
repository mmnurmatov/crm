package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.NotificationTypeService;

import java.util.List;

@Controller
@RequestMapping("directory/notification-type")
public class NotificationTypeController extends BaseController {

    @Autowired
    NotificationTypeService notificationTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('NotificationTypeRead')")
    public ModelAndView index(
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<NotificationType> notificationTypes = notificationTypeService.findAll();
        modelAndView.setViewName("/pages/directories/notification-types/index");
        modelAndView.addObject("notificationTypes", notificationTypes);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationTypeCreate')")
    public RedirectView createNotificationType(
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "icon") String icon,
                                   @RequestParam(name = "classes") String classes,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        NotificationType notificationType = new NotificationType();
        notificationType.setName(name);
        notificationType.setIcon(icon);
        notificationType.setClasses(classes);
        notificationTypeService.saveNotificationType(notificationType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/notification-type/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationTypeUpdate')")
    public RedirectView updateNotificationType(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "nameEdit") String name,
                                   @RequestParam(name = "iconEdit") String icon,
                                   @RequestParam(name = "classesEdit") String classes,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        NotificationType notificationType = notificationTypeService.getNotificationTypeById(id);
        notificationType.setId(id);
        notificationType.setName(name);
        notificationType.setIcon(icon);
        notificationType.setClasses(classes);
        notificationTypeService.saveNotificationType(notificationType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/notification-type/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationTypeDelete')")
    public RedirectView deleteNotificationType(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        notificationTypeService.deleteNotificationType(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/notification-type/");
    }
    
}
