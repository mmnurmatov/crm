package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.MoneyTypeService;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("directory/money-types")
public class MoneyTypeController extends BaseController {

    @Autowired
    MoneyTypeService moneyTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('MoneyTypeRead')")
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

        List<MoneyType> moneyTypes = moneyTypeService.findAll();
        modelAndView.setViewName("/pages/directories/money-types/index");
        modelAndView.addObject("moneyTypes", moneyTypes);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MoneyTypeCreate')")
    public RedirectView createMoneyType(
                                   @RequestParam(name = "nameUz") String nameUz,
                                   @RequestParam(name = "nameRu") String nameRu,
                                   @RequestParam(name = "nameEn") String nameEn,
                                   @RequestParam(name = "code") String code,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        MoneyType moneyType = new MoneyType();
        moneyType.setNameUz(nameUz);
        moneyType.setNameRu(nameRu);
        moneyType.setNameEn(nameEn);
        moneyType.setCode(code);
        moneyType.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        moneyTypeService.saveMoneyType(moneyType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/money-types/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MoneyTypeUpdate')")
    public RedirectView updateMoneyType(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "nameUzEdit") String nameUz,
                                   @RequestParam(name = "nameRuEdit") String nameRu,
                                   @RequestParam(name = "nameEnEdit") String nameEn,
                                   @RequestParam(name = "codeEdit") String code,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        MoneyType moneyType = moneyTypeService.getMoneyTypeById(id);
        moneyType.setId(id);
        moneyType.setNameUz(nameUz);
        moneyType.setNameRu(nameRu);
        moneyType.setNameEn(nameEn);
        moneyType.setCode(code);
        moneyType.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        moneyTypeService.saveMoneyType(moneyType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/money-types/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('MoneyTypeDelete')")
    public RedirectView deleteMoneyType(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        moneyTypeService.deleteMoneyType(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/money-types/");
    }

}
