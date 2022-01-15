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
import uz.isd.javagroup.grandcrm.entity.directories.CompanyType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.CompanyTypeService;

import java.util.List;

@Controller
@RequestMapping("directory/company-types")
public class CompanyTypeController extends BaseController {

    @Autowired
    CompanyTypeService companyTypeService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CompanyTypeRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<CompanyType> companyTypes = companyTypeService.findAll();
        modelAndView.setViewName("/pages/directories/company-types/index");
        modelAndView.addObject("companyTypes", companyTypes);

//        modelAndView.addObject("addBtnShow", true);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyTypeCreate')")
    public RedirectView createCompanyType(@RequestParam(name = "nameUz") String nameUz,
                                          @RequestParam(name = "nameRu") String nameRu,
                                          @RequestParam(name = "nameEn") String nameEn,
                                          RedirectAttributes redirectAttributes) {
        CompanyType companyType = new CompanyType();
        companyType.setNameRu(nameRu);
        companyType.setNameUz(nameUz);
        companyType.setNameEn(nameEn);
        companyTypeService.saveCompanyType(companyType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/company-types/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyTypeUpdate')")
    public RedirectView updateCompanyType(@RequestParam(name = "id") Long id,
                                          @RequestParam(name = "nameUzEdit") String nameUz,
                                          @RequestParam(name = "nameRuEdit") String nameRu,
                                          @RequestParam(name = "nameEnEdit") String nameEn,
                                          RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        CompanyType companyType = companyTypeService.getCompanyTypeById(id);
        companyType.setId(id);
        companyType.setNameRu(nameRu);
        companyType.setNameUz(nameUz);
        companyType.setNameEn(nameEn);
        companyTypeService.saveCompanyType(companyType);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/company-types/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyTypeDelete')")
    public RedirectView deleteCompanyType(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        companyTypeService.deleteCompanyType(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/company-types/");
    }
}
