package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationTemplate;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.NotificationTemplateService;
import uz.isd.javagroup.grandcrm.services.directories.NotificationTypeService;

import java.util.List;

@Controller
@RequestMapping("directory/notification-template")
public class NotificationTemplateController  extends BaseController {

    @Autowired
    NotificationTemplateService notificationTemplateService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('NotificationTemplateRead')")
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

        List<NotificationTemplate> notificationTemplates = notificationTemplateService.findAll();
        modelAndView.setViewName("/pages/directories/notification-templates/index");
        modelAndView.addObject("notificationTemplates", notificationTemplates);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationTemplateCreate')")
    public RedirectView createNotificationTemplate(
                                   @RequestParam(name = "titleUz") String titleUz,
                                   @RequestParam(name = "titleRu") String titleRu,
                                   @RequestParam(name = "titleEn") String titleEn,
                                   @RequestParam(name = "contentUz") String contentUz,
                                   @RequestParam(name = "contentRu") String contentRu,
                                   @RequestParam(name = "contentEn") String contentEn,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setTitleUz(titleUz);
        notificationTemplate.setTitleRu(titleRu);
        notificationTemplate.setTitleEn(titleEn);
        notificationTemplate.setContentUz(contentUz);
        notificationTemplate.setContentRu(contentRu);
        notificationTemplate.setContentEn(contentEn);
        notificationTemplateService.saveNotificationTemplate(notificationTemplate);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/notification-template/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationTemplateUpdate')")
    public RedirectView updateNotificationTemplate(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "titleUzEdit") String titleUz,
                                   @RequestParam(name = "titleRuEdit") String titleRu,
                                   @RequestParam(name = "titleEnEdit") String titleEn,
                                   @RequestParam(name = "contentUzEdit") String contentUz,
                                   @RequestParam(name = "contentRuEdit") String contentRu,
                                   @RequestParam(name = "contentEnEdit") String contentEn,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        NotificationTemplate notificationTemplate = notificationTemplateService.getNotificationTemplateById(id);
        notificationTemplate.setId(id);
        notificationTemplate.setTitleUz(titleUz);
        notificationTemplate.setTitleRu(titleRu);
        notificationTemplate.setTitleEn(titleEn);
        notificationTemplate.setContentUz(contentUz);
        notificationTemplate.setContentRu(contentRu);
        notificationTemplate.setContentEn(contentEn);
        notificationTemplateService.saveNotificationTemplate(notificationTemplate);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/notification-template/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('NotificationTemplateDelete')")
    public RedirectView deleteNotificationTemplate(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        notificationTemplateService.deleteNotificationTemplate(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/notification-template/");
    }

}
