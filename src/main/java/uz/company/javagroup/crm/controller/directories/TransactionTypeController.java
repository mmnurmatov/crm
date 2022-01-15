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
import uz.isd.javagroup.grandcrm.entity.directories.TransactionType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.TransactionTypeService;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("directory/transaction-types")
public class TransactionTypeController extends BaseController {

    @Autowired
    TransactionTypeService transactionTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('TransactionTypeRead')")
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

        List<TransactionType> transactionTypes = transactionTypeService.findAll();

        modelAndView.addObject("transactionTypes", transactionTypes);
        modelAndView.setViewName("/pages/directories/transaction-types/index");

        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TransactionTypeCreate')")
    public RedirectView createMoneyType(
            @RequestParam(name = "nameUz") String nameUz,
            @RequestParam(name = "nameRu") String nameRu,
            @RequestParam(name = "nameEn") String nameEn,
            RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        TransactionType transactionType = new TransactionType();
        transactionType.setNameUz(nameUz);
        transactionType.setNameRu(nameRu);
        transactionType.setNameEn(nameEn);
        transactionType.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        transactionTypeService.saveTransactionType(transactionType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/transaction-types/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TransactionTypeUpdate')")
    public RedirectView updateMoneyType(@RequestParam(name = "id") Long id,
                                        @RequestParam(name = "nameUzEdit") String nameUz,
                                        @RequestParam(name = "nameRuEdit") String nameRu,
                                        @RequestParam(name = "nameEnEdit") String nameEn,
                                        RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        TransactionType transactionType = transactionTypeService.getTransactionTypeById(id);
        transactionType.setId(id);
        transactionType.setNameUz(nameUz);
        transactionType.setNameRu(nameRu);
        transactionType.setNameEn(nameEn);
        transactionType.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        transactionTypeService.saveTransactionType(transactionType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/transaction-types/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TransactionTypeDelete')")
    public RedirectView deleteMoneyType(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        transactionTypeService.deleteTransactionType(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/transaction-types/");
    }

}
