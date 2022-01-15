package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.entity.modules.Transaction;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.CacheService;
import uz.isd.javagroup.grandcrm.services.modules.TransactionService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

import static uz.isd.javagroup.grandcrm.entity.modules.Transaction.TransactionStatus.*;

@Controller
@RequestMapping("module/cache-confirmation")
public class CacheConfirmation extends BaseController {

    @Autowired
    TransactionService transactionService;
    @Autowired
    CacheService cacheService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<Transaction> successTransactions = transactionService.findAllByTransactionStatus(SUCCESS);
        List<Transaction> pendingTransactions = transactionService.findAllByTransactionStatus(PENDING);
        List<Transaction> cancelledTransactions = transactionService.findAllByTransactionStatus(CANCELLED);
        List<Transaction> failedTransactions = transactionService.findAllByTransactionStatus(FAILED);

        modelAndView.addObject("successTransactions", successTransactions);
        modelAndView.addObject("pendingTransactions", pendingTransactions);
        modelAndView.addObject("cancelledTransactions", cancelledTransactions);
        modelAndView.addObject("failedTransactions", failedTransactions);

        modelAndView.setViewName("/pages/modules/cache-confirmations/accept");
        return modelAndView;
    }

    @RequestMapping(path = "/success/{transactionId}", method = RequestMethod.GET)
    @Transactional
    public RedirectView success(@PathVariable("transactionId") Long transactionId,
                                RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction.getCache() != null) {
            Cache cache = transaction.getCache();
            Cache kreditProccessResult = transactionService.proccessKredit(cache, transaction.getKredit(), transaction.getMoneyType());
            if (kreditProccessResult != null) {
                transaction.setTransactionStatus(SUCCESS);
                transaction.setCache(kreditProccessResult);
                redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
                redirectAttributes.addFlashAttribute("error", null);
                redirectAttributes.addFlashAttribute("message", "Amaliyot muvvaffaqiyatli bajarildi!!!");
            } else {
                transaction.setTransactionStatus(Transaction.TransactionStatus.FAILED);
                cache.setLastTransactionTime(new Timestamp(System.currentTimeMillis()));
                cacheService.saveCache(cache);
                transaction.setCache(cache);
                redirectAttributes.addFlashAttribute("status", this.getStatusError());
                redirectAttributes.addFlashAttribute("error", true);
                redirectAttributes.addFlashAttribute("message", "Amaliyot muvvaffaqiyatli bajarilmadi!!!");
            }
        } else throw new RecordNotFoundException("Cache is not found...");
        return new RedirectView("/module/cache-confirmation/");
    }

    @RequestMapping(path = "/cancel/{transactionId}", method = RequestMethod.GET)
    public RedirectView cancel(@PathVariable("transactionId") Long transactionId,
                               RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        Transaction transaction = transactionService.getTransactionById(transactionId);
        transaction.setTransactionStatus(CANCELLED);
        transactionService.saveTransaction(transaction);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("error", null);
        return new RedirectView("/module/cache-confirmation/");
    }
}
