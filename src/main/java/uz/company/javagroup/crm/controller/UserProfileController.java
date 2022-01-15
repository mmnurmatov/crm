package uz.isd.javagroup.grandcrm.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import uz.isd.javagroup.grandcrm.entity.directories.*;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.*;
import uz.isd.javagroup.grandcrm.services.modules.UserService;
import uz.isd.javagroup.grandcrm.utility.NotificationSending;
import uz.isd.javagroup.grandcrm.utility.SecurityUtility;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class UserProfileController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationDataService notificationDataService;

    @Autowired
    private NotificationTemplateService notificationTemplateService;

    @Autowired
    private NotificationTypeService notificationTypeService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationUserService notificationUserService;

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    ServletContext servletContext;

    private final TemplateEngine templateEngine;

    public UserProfileController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    @RequestMapping(path = "/user-profile", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('READ')")
    public ModelAndView userProfile(@ModelAttribute("status") String typesData,
                                    @ModelAttribute("message") String messageData,
                                    ModelAndView modelAndView,
                                    HttpSession session) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        User user = this.getAuthUserData();
        modelAndView.addObject("userProfile", user);
        System.out.println(this.getAuthUserId());


        String userPictureName = userPictureName = "/user-photos/" + user.getId() + "/" + user.getImageUrl();
        boolean isFound = user.getImageUrl().indexOf("default.png") !=-1? true: false;

        if(!isFound)
            modelAndView.addObject("userPictureName", userPictureName);
        else
            modelAndView.addObject("userPictureName", "/app-assets/images/default.png");


        if (!isFound){
            session.setAttribute("userImage", "/user-photos/" + user.getId() + "/" + user.getImageUrl());
        }else {
            session.setAttribute("userImage", "/app-assets/images/default.png");
        }

        modelAndView.setViewName("/pages/user-profile");

        return modelAndView;
    }


    @RequestMapping(path = "/updateFoto", method = RequestMethod.POST)
    public RedirectView updateUserFoto( @RequestParam(name = "userImage") MultipartFile userImage,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException, IOException {

        User user = userService.getUserById(this.getAuthUserId());
        user.setUpdatedAt(new Date());
        userService.updateUser(user, userImage);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/user-profile");
    }


    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('UPDATE')")
    public RedirectView updateUser(@RequestParam(name = "firstName") String firstName,
                                   @RequestParam(name = "secondName") String secondName,
                                   @RequestParam(name = "lastName") String lastName,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "phoneNumber") String phoneNumber,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        User user = userService.getUserById(this.getAuthUserId());
        user.setFirstName(firstName);
        user.setSecondName(secondName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setUpdatedAt(new Date());
        userService.save(user);

        Notification notification = new Notification();
        notification.setNotificationTemplate(notificationTemplateService.getNotificationTemplateById(1L));
        notification.setNotificationType(notificationTypeService.getNotificationTypeById(1L));
        notification.setCreatedAt(new Date());
        notificationService.saveNotification(notification);

        NotificationData notificationData = new NotificationData();
        notificationData.setNotification(notification);
        notificationData.setKey("phoneNumber");
        notificationData.setValue(phoneNumber);
        notificationDataService.saveNotificationData(notificationData);

        NotificationSending notificationSending = new NotificationSending(notification, user);
        notificationUserService.saveNotificationUser(notificationSending.sendingNotification());

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/logout");
    }

    @RequestMapping(path = "/saveUserPassword", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('Save Password')")
    public String updateUser(@RequestParam(name = "newPassword") String newPassword) {
        User user = this.getAuthUserData();
        user.setPassword(SecurityUtility.passwordEncoder().encode(newPassword));
        userService.save(user);
        return "redirect:/user-profile";
    }

    @RequestMapping(path = "/emailSent", method = RequestMethod.GET)
    public String registerUser(ModelAndView modelAndView) {

        User existingUser = this.getAuthUserData();
        if (existingUser == null) {
            modelAndView.addObject("message", "Bunday foydalanuvchi mavjud emas");
            modelAndView.addObject("status", false);
            return "redirect:/user-profile";
        }
        String token = UUID.randomUUID().toString();
        existingUser.setToken(token);
        userService.save(existingUser);
        this.sendEmailMessage(existingUser.getUsername(), existingUser.getEmail(), existingUser.getToken());

        modelAndView.addObject("message", "Xabar muvaffaqiyatli yuborildi.");
        modelAndView.addObject("status", true);
        return "redirect:/user-profile";
    }

    @RequestMapping(path = "/confirm-account", method = RequestMethod.GET)
    public String confirmUserAccount(@RequestParam("username") String username, @RequestParam("token") String token, ModelAndView modelAndView) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            modelAndView.addObject("message", "Foydalanubchi mavjud emas. username: " + username);
            modelAndView.addObject("status", false);
            return "redirect:/user-profile";
        }
        if (!user.getToken().equals(token)) {
            modelAndView.addObject("message", "Token mos kelmadi.");
            modelAndView.addObject("status", false);
            return "redirect:/user-profile";
        }
        user.setIsEmailValid(Boolean.TRUE);
        userService.save(user);
        modelAndView.addObject("message", "Mail validatsiyadan o'tdi.");
        modelAndView.addObject("status", true);
        return "redirect:/user-profile";
    }

}
