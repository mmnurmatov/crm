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
import uz.isd.javagroup.grandcrm.entity.directories.UserStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.UserStatusService;

import java.util.List;

@Controller
@RequestMapping("directory/user-statuses")
public class UserStatusController extends BaseController {

    @Autowired
    UserStatusService userStatusService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('UserStatusRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String statusData, @ModelAttribute("message") String messageData) {

        if (statusData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", statusData);
            modelAndView.addObject("message", messageData);
        }

        List<UserStatus> userStatuses = userStatusService.findAll();
        modelAndView.setViewName("/pages/directories/user-statuses/index");
        modelAndView.addObject("userStatuses", userStatuses);

        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('UserStatusCreate')")
    public RedirectView createUserStatus(@RequestParam(name = "nameUz") String nameUz,
                                         @RequestParam(name = "nameRu") String nameRu,
                                         @RequestParam(name = "nameEn") String nameEn,
                                         @RequestParam(name = "description") String description,
                                         RedirectAttributes redirectAttributes) {
        UserStatus userStatus = new UserStatus();
        userStatus.setNameRu(nameRu);
        userStatus.setNameUz(nameUz);
        userStatus.setNameEn(nameEn);
        userStatus.setDescription(description);

        userStatusService.saveUserStatus(userStatus);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/user-statuses/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('UserStatusUpdate')")
    public RedirectView updateUserStatus(@RequestParam(name = "id") Long id,
                                         @RequestParam(name = "nameUzEdit") String nameUz,
                                         @RequestParam(name = "nameRuEdit") String nameRu,
                                         @RequestParam(name = "nameEnEdit") String nameEn,
                                         @RequestParam(name = "descriptionEdit") String description,
                                         RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        UserStatus userStatus = userStatusService.getUserStateByid(id);
        userStatus.setId(id);
        userStatus.setNameRu(nameRu);
        userStatus.setNameUz(nameUz);
        userStatus.setNameEn(nameEn);
        userStatus.setDescription(description);
        userStatusService.saveUserStatus(userStatus);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/user-statuses/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('UserStatusDelete')")
    public RedirectView deleteUserStatus(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        userStatusService.deleteUserStatus(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/user-statuses/");
    }
}
