package uz.isd.javagroup.grandcrm.controller.modules;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;
import uz.isd.javagroup.grandcrm.entity.directories.TransactionType;
import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.entity.modules.Transaction;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.MoneyTypeService;
import uz.isd.javagroup.grandcrm.services.directories.TransactionStatusService;
import uz.isd.javagroup.grandcrm.services.directories.TransactionTypeService;
import uz.isd.javagroup.grandcrm.services.modules.CacheService;
import uz.isd.javagroup.grandcrm.services.modules.TransactionService;
import uz.isd.javagroup.grandcrm.services.modules.UserService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static uz.isd.javagroup.grandcrm.entity.modules.Transaction.TransactionStatus.PENDING;

@Controller
@RequestMapping("module/transaction")
public class TransactionController extends BaseController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    CacheService cacheService;

    @Autowired
    UserService userService;

    @Autowired
    TransactionTypeService transactionTypeService;

    @Autowired
    TransactionStatusService transactionStatusService;

    @Autowired
    MoneyTypeService moneyTypeService;

    @RequestMapping(path = {"/{cacheId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('TransactionRead')")
    public ModelAndView index(ModelAndView modelAndView,
                              @PathVariable(name = "cacheId") Optional<Long> cacheId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("error") String error,
                              @ModelAttribute("message") String messageData) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
            modelAndView.addObject("error", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
            modelAndView.addObject("error", true);
        }

        User user = this.getAuthUserData();

        List<Transaction> transactions = transactionService.findAllByCacheId(cacheId.get());
        Cache cache = cacheService.getCacheById(cacheId.get());


        List<TransactionType> transactionTypes = transactionTypeService.findAll();
        List<MoneyType> moneyTypes = moneyTypeService.findAll();

        modelAndView.addObject("transactions", transactions);
        modelAndView.addObject("cache", cache);
        modelAndView.addObject("transactionTypes", transactionTypes);
        modelAndView.addObject("moneyTypes", moneyTypes);
        modelAndView.addObject("user", user);

        modelAndView.setViewName("/pages/modules/transactions/index");
        return modelAndView;
    }

    @RequestMapping(path = "/debet/create", method = RequestMethod.POST)
    @Transactional
    public RedirectView createDebet(@RequestParam(name = "userId") Optional<Long> userId,
                                    @RequestParam(name = "cacheId") Optional<Long> cacheId,
                                    @RequestParam(name = "transactionTypeId") Optional<Long> transactionTypeId,
                                    @RequestParam(name = "moneyTypeId") Optional<Long> moneyTypeId,
                                    @RequestParam(name = "debet") BigDecimal debet,
                                    @RequestParam(name = "description") String description,
                                    RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        Transaction transaction = new Transaction();

        transaction.setDescription(description);
        transaction.setDebet(debet);

        transaction.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        if (userId.isPresent()) {
            transaction.setUser(userService.getUserById(userId.get()));
        } else {
            throw new RecordNotFoundException("User is not found with Id" + userId.get());
        }

        if (cacheId.isPresent()) {

            Cache cache = cacheService.getCacheById(cacheId.get());
            Cache debetProccessResult = transactionService.proccessDebet(cache, debet, moneyTypeId);

            if (debetProccessResult != null) {
                transaction.setTransactionStatus(Transaction.TransactionStatus.SUCCESS);
                transaction.setCache(debetProccessResult);
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

        } else {
            throw new RecordNotFoundException("Cache is not found with Id" + cacheId.get());
        }

        if (transactionTypeId.isPresent()) {
            transaction.setTransactionType(transactionTypeService.getTransactionTypeById(transactionTypeId.get()));
        } else {
            throw new RecordNotFoundException("Transaction Type is not found with Id" + cacheId.get());
        }

        if (moneyTypeId.isPresent()) {
            transaction.setMoneyType(moneyTypeService.getMoneyTypeById(moneyTypeId.get()));
        } else {
            throw new RecordNotFoundException("Money Type is not found with Id" + cacheId.get());
        }

        transactionService.saveTransaction(transaction);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/transaction/" + cacheId.get());
    }


    @RequestMapping(path = "/kredit/create", method = RequestMethod.POST)
    public RedirectView createArea(@RequestParam(name = "userId") Optional<Long> userId,
                                   @RequestParam(name = "cacheId") Optional<Long> cacheId,
                                   @RequestParam(name = "transactionTypeId") Optional<Long> transactionTypeId,
                                   @RequestParam(name = "moneyTypeId") Optional<Long> moneyTypeId,
                                   @RequestParam(name = "kredit") BigDecimal kredit,
                                   @RequestParam(name = "description") String description,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setKredit(kredit);
        transaction.setTransactionStatus(PENDING);
        transaction.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        if (userId.isPresent()) transaction.setUser(userService.getUserById(userId.get()));
        else throw new RecordNotFoundException("User is not found with Id");
        if (cacheId.isPresent()) transaction.setCache(cacheService.getCacheById(cacheId.get()));
        else throw new RecordNotFoundException("Cache is not found with Id");
        if (transactionTypeId.isPresent())
            transaction.setTransactionType(transactionTypeService.getTransactionTypeById(transactionTypeId.get()));
        else throw new RecordNotFoundException("Transaction Type is not found with Id");
        if (moneyTypeId.isPresent()) transaction.setMoneyType(moneyTypeService.getMoneyTypeById(moneyTypeId.get()));
        else throw new RecordNotFoundException("Money Type is not found with Id");
        transactionService.saveTransaction(transaction);
        return new RedirectView("/module/transaction/" + cacheId.get());
    }
}
