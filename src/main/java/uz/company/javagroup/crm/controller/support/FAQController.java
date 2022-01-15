package uz.isd.javagroup.grandcrm.controller.support;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.support.FAQ;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.support.FAQService;

import java.util.List;

@Controller
@RequestMapping("support/faq")
public class FAQController extends BaseController {

    @Autowired
    FAQService faqService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('FAQRead')")
    public ModelAndView index(
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView
    ) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<FAQ> faqs = faqService.findAll();
        modelAndView.setViewName("/pages/supports/faq/index");
        modelAndView.addObject("faqs", faqs);
        return modelAndView;
    }

}
