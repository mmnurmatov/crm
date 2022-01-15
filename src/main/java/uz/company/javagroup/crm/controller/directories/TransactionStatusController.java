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
import uz.isd.javagroup.grandcrm.entity.directories.TransactionStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.TransactionStatusService;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("directory/transaction-statuses")
public class TransactionStatusController extends BaseController {

    @Autowired
    TransactionStatusService transactionStatusService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('TransactionStatusRead')")
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

        List<TransactionStatus> transactionStatuses = transactionStatusService.findAll();

        modelAndView.addObject("transactionStatuses", transactionStatuses);
        modelAndView.setViewName("/pages/directories/transaction-statuses/index");

        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TransactionStatusCreate')")
    public RedirectView createTransactionStatus(
            @RequestParam(name = "nameUz") String nameUz,
            @RequestParam(name = "nameRu") String nameRu,
            @RequestParam(name = "nameEn") String nameEn,
            RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        TransactionStatus transactionStatus = new TransactionStatus();
        transactionStatus.setNameUz(nameUz);
        transactionStatus.setNameRu(nameRu);
        transactionStatus.setNameEn(nameEn);
        transactionStatus.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        transactionStatusService.saveTransactionStatus(transactionStatus);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/transaction-statuses/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TransactionStatusUpdate')")
    public RedirectView updateTransactionStatus(@RequestParam(name = "id") Long id,
                                        @RequestParam(name = "nameUzEdit") String nameUz,
                                        @RequestParam(name = "nameRuEdit") String nameRu,
                                        @RequestParam(name = "nameEnEdit") String nameEn,
                                        RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        TransactionStatus transactionStatus = transactionStatusService.getTransactionStatusById(id);
        transactionStatus.setId(id);
        transactionStatus.setNameUz(nameUz);
        transactionStatus.setNameRu(nameRu);
        transactionStatus.setNameEn(nameEn);
        transactionStatus.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        transactionStatusService.saveTransactionStatus(transactionStatus);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/transaction-statuses/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('TransactionStatusDelete')")
    public RedirectView deleteTransactionStatus(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        transactionStatusService.deleteTransactionStatus(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/transaction-statuses/");
    }


}
