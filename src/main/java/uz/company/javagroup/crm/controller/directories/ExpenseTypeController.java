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
import uz.isd.javagroup.grandcrm.entity.directories.ExpenseType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.ExpenseTypeService;

import java.util.List;

@Controller
@RequestMapping("directory/expense-types")
public class ExpenseTypeController extends BaseController {


    @Autowired
    ExpenseTypeService expenseTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ExpenseTypeRead')")
    public ModelAndView index(@ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<ExpenseType> expenseTypes = expenseTypeService.findAll();
        modelAndView.setViewName("/pages/directories/expense-types/index");
        modelAndView.addObject("expenseTypes", expenseTypes);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ExpenseTypeCreate')")
    public RedirectView createExpenseType(@RequestParam(name = "nameUz") String nameUz,
                                          @RequestParam(name = "nameRu") String nameRu,
                                          @RequestParam(name = "nameEn") String nameEn,
                                          @RequestParam(name = "type") Integer type,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ExpenseType expenseType = new ExpenseType();
        expenseType.setNameUz(nameUz);
        expenseType.setNameRu(nameRu);
        expenseType.setNameEn(nameEn);
        expenseType.setType(type);
        expenseTypeService.saveExpenseType(expenseType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/expense-types/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ExpenseTypeUpdate')")
    public RedirectView updateExpenseType(@RequestParam(name = "id") Long id,
                                          @RequestParam(name = "nameUzEdit") String nameUz,
                                          @RequestParam(name = "nameRuEdit") String nameRu,
                                          @RequestParam(name = "nameEnEdit") String nameEn,
                                          @RequestParam(name = "typeEdit") Integer type,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        ExpenseType expenseType = expenseTypeService.getExpenseTypeById(id);
        expenseType.setId(id);
        expenseType.setNameUz(nameUz);
        expenseType.setNameRu(nameRu);
        expenseType.setNameEn(nameEn);
        expenseType.setType(type);
        expenseTypeService.saveExpenseType(expenseType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/expense-types/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ExpenseTypeDelete')")
    public RedirectView deleteExpenseType(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        expenseTypeService.deleteExpenseType(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/expense-types/");
    }
}
