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
import uz.isd.javagroup.grandcrm.entity.support.FAQ;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.support.FAQService;

import java.util.List;

@Controller
@RequestMapping("directory/tech-support/faq")
public class FAQTechSupportController extends BaseController {

    @Autowired
    FAQService faqService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('FAQTechSupportRead')")
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
        modelAndView.setViewName("/pages/directories/tech-supports/index");
        modelAndView.addObject("faqs", faqs);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('FAQTechSupportCreate')")
    public RedirectView createFAQ(
            @RequestParam(name = "questionRu") String questionRu,
            @RequestParam(name = "questionUz") String questionUz,
            @RequestParam(name = "questionEn") String questionEn,
            @RequestParam(name = "answerRu") String answerRu,
            @RequestParam(name = "answerUz") String answerUz,
            @RequestParam(name = "answerEn") String answerEn,
            @RequestParam(name = "ordering") int ordering,
            RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        FAQ faq = new FAQ();
        faq.setQuestionRu(questionRu);
        faq.setQuestionUz(questionUz);
        faq.setQuestionEn(questionEn);
        faq.setAnswerRu(answerRu);
        faq.setAnswerUz(answerUz);
        faq.setAnswerEn(answerEn);
        faq.setOrdering(ordering);
        faqService.saveFAQ(faq);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/tech-support/faq/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('FAQTechSupportUpdate')")
    public RedirectView updateFAQ(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "questionRuEdit") String questionRu,
            @RequestParam(name = "questionUzEdit") String questionUz,
            @RequestParam(name = "questionEnEdit") String questionEn,
            @RequestParam(name = "answerRuEdit") String answerRu,
            @RequestParam(name = "answerUzEdit") String answerUz,
            @RequestParam(name = "answerEnEdit") String answerEn,
            @RequestParam(name = "orderingEdit") int orderingEdit,
            RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        FAQ faq = faqService.getFAQById(id);
        faq.setId(id);
        faq.setQuestionRu(questionRu);
        faq.setQuestionUz(questionUz);
        faq.setQuestionEn(questionEn);
        faq.setAnswerRu(answerRu);
        faq.setAnswerUz(answerUz);
        faq.setAnswerEn(answerEn);
        faq.setOrdering(orderingEdit);
        faqService.saveFAQ(faq);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/tech-support/faq/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('FAQTechSupportDelete')")
    public RedirectView deleteArea(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        faqService.deleteFAQ(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/tech-support/faq/");
    }

}
