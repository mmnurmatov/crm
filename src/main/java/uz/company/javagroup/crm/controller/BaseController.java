package uz.isd.javagroup.grandcrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.repository.modules.UserRepository;
import uz.isd.javagroup.grandcrm.services.MailConstructor;

import java.util.Optional;

@Controller
public class BaseController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MailConstructor mailConstructor;
    @Value("${spring.mail.username}")
    String from;
    @Value("${url.to.confirm.mail}")
    String url;
    private Long authUserId;
    private String authUserLangCode = "uz";

    public String getAuthUserLangCode() {
        return authUserLangCode;
    }

    public void setAuthUserLangCode(String authUserLangCode) {
        this.authUserLangCode = authUserLangCode;
    }

    public User getAuthUserData() {
        Optional<User> user = userRepository.findById(this.getAuthUserId());
        return user.get();
    }

    public Long getAuthUserId() {
        this.setAuthUserId();
        return authUserId;
    }

    public void setAuthUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUserData = (User) authentication.getPrincipal();
        this.authUserId = authUserData.getId();
    }

    public String getStatusSuccess() {
        return "SUCCESS";
    }

    public String getStatusError() {
        return "ERROR";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("userData", this.getAuthUserData());
    }

    public void sendEmailMessage(String username, String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Validate Your Email to complete Registration");
        mailMessage.setFrom(from);
        mailMessage.setText("To confirm your account, please click here : " + url + "/confirm-account?username=" + username + "&token=" + token);
        mailConstructor.sendEmail(mailMessage);
    }
}
