package uz.isd.javagroup.grandcrm.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletResponse response){

        ModelAndView modelAndView = new ModelAndView();

        if(response.getStatus() == HttpStatus.NOT_FOUND.value()){
            modelAndView.setViewName("/pages/error");
        }else if(response.getStatus() == HttpStatus.FORBIDDEN.value()){
            modelAndView.setViewName("/pages/permission-error");
        }else if (response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            modelAndView.setViewName("/pages/error");
        }else {
            modelAndView.setViewName("/pages/error");
        }
        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return "/pages/error";
    }
}
