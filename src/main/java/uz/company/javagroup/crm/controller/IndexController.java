package uz.isd.javagroup.grandcrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.entity.directories.UserFiles;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.services.modules.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView index(@RequestParam(name = "lang") Optional<String> lang,
                              ModelAndView modelAndView,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              HttpSession session) {

        if (lang.isPresent()) {
            System.out.println("System Lang Selected: " + lang.get());
            this.setAuthUserLangCode(lang.get());
            System.out.println(this.getAuthUserLangCode());
        }

        User user = this.getAuthUserData();

        session.setAttribute("fullname", user.getFirstName() +" "+ user.getSecondName());
        session.setAttribute("role", user.getRole().getNameUz());

        boolean isFound = true;
        if (user.getImageUrl() != null){
            isFound = user.getImageUrl().indexOf("default.png") !=-1? true: false;
        }


        if (!isFound){
            session.setAttribute("userImage", "/user-photos/" + user.getId() + "/" + user.getImageUrl());
        }else {
            session.setAttribute("userImage", "/app-assets/images/default.png");
        }

        modelAndView.setViewName("/pages/index");
        return modelAndView;
    }


    @RequestMapping(path = "/module-in-development", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView) {

        modelAndView.setViewName("/pages/module-in-development");
        return modelAndView;
    }


}
