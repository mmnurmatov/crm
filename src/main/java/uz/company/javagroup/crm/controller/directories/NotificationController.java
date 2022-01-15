package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.*;
import uz.isd.javagroup.grandcrm.services.directories.NotificationService;
import uz.isd.javagroup.grandcrm.services.directories.NotificationTemplateService;
import uz.isd.javagroup.grandcrm.services.directories.NotificationTypeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("directory/notification")
public class NotificationController extends BaseController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    NotificationTypeService notificationTypeService;

    @Autowired
    NotificationTemplateService notificationTemplateService;


    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('NotificationRead')")
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
        Long userId = getAuthUserId();
        List<Object[]> objects = notificationService.allByUserId(userId);

        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] o : objects) {
            Notification notification = (Notification) o[0];
            NotificationData notificationData = (NotificationData) o[1];
            NotificationUser notificationUser = (NotificationUser) o[2];
            NotificationTemplate notificationTemplate = (NotificationTemplate) o[3];
            NotificationType notificationType = (NotificationType) o[4];

            Map<String, Object> map = new HashMap<>();
            map.put("notificationId", notification.getId());
            map.put("notificationTemplateTitle", notificationTemplate.getTitleUz());
            map.put("notificationTemplateContent", notificationTemplate.getContentUz().replaceFirst(notificationData.getKey(), notificationData.getValue()));
            map.put("notificationTypeName", notificationType.getName());
            map.put("notificationDate", notification.getCreatedAt());
            map.put("isView", notificationUser.getIsView());
            response.add(map);
        }
        modelAndView.addObject("response", response);
        modelAndView.setViewName("/pages/directories/notifications/index");
        return modelAndView;
    }


}
