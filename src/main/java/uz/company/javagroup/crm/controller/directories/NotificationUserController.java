package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationUser;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.services.directories.NotificationService;
import uz.isd.javagroup.grandcrm.services.directories.NotificationUserService;
import uz.isd.javagroup.grandcrm.services.modules.UserService;

import java.util.List;

@Controller
@RequestMapping("directory/notification-users")
public class NotificationUserController extends BaseController {

    @Autowired
    NotificationUserService notificationUserService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserService userService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('NotificationUserRead')")
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

        List<Notification> notifications = notificationService.findAll();
        List<User> users = userService.findAll();
        List<NotificationUser> notificationUsers = notificationUserService.findAll();
        modelAndView.setViewName("/pages/directories/notification-users/index");
        modelAndView.addObject("notifications", notifications);
        modelAndView.addObject("users", users);
        modelAndView.addObject("notificationUsers", notificationUsers);
        return modelAndView;
    }

}
