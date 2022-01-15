package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationData;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.NotificationDataService;
import uz.isd.javagroup.grandcrm.services.directories.NotificationService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("directory/notification-data")
public class NotificationDataController extends BaseController {

    @Autowired
    NotificationDataService notificationDataService;

    @Autowired
    NotificationService notificationService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('NotificationDataRead')")
    public ModelAndView index(@PathVariable(name = "countryId") Optional<Long> countryId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Notification> notifications = notificationService.findAll();
        List<NotificationData> notificationData = notificationDataService.findAll();
        modelAndView.setViewName("/pages/directories/notification-data/index");
        modelAndView.addObject("notifications", notifications);
        modelAndView.addObject("notificationData", notificationData);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationDataCreate')")
    public RedirectView createArea(@RequestParam(name = "notificationId") Long notificationId,
                                   @RequestParam(name = "key") String key,
                                   @RequestParam(name = "value") String value,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        NotificationData notificationData = new NotificationData();
        notificationData.setKey(key);
        notificationData.setValue(value);
        notificationData.setNotification(notificationService.getNotificationById(notificationId));
        notificationDataService.saveNotificationData(notificationData);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/notification-data/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationDataUpdate')")
    public RedirectView updateArea(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "notificationEdit") Long notificationId,
                                   @RequestParam(name = "keyEdit") String key,
                                   @RequestParam(name = "valueEdit") String value,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        NotificationData notificationData = notificationDataService.getNotificationDataById(id);
        notificationData.setId(id);
        notificationData.setKey(key);
        notificationData.setValue(value);
        notificationData.setNotification(notificationService.getNotificationById(notificationId));
        notificationDataService.saveNotificationData(notificationData);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/notification-data/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationDataDelete')")
    public RedirectView deleteArea(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        notificationDataService.deleteNotificationData(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/notification-data/");
    }

}
