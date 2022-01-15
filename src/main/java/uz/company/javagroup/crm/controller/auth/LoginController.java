package uz.isd.javagroup.grandcrm.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String login(Model model, HttpSession httpSession) {
        return "/pages/login";
    }
}
