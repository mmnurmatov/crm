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
import uz.isd.javagroup.grandcrm.entity.directories.CompanyStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.CompanyStatusService;

import java.util.List;

@Controller
@RequestMapping("directory/company-statuses")
public class CompanyStatusController extends BaseController {

    @Autowired
    CompanyStatusService companyStatusService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CompanyStatusRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String statusData, @ModelAttribute("message") String messageData) {

        if (statusData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", statusData);
            modelAndView.addObject("message", messageData);
        }

        List<CompanyStatus> companyStatuses = companyStatusService.findAll();
        modelAndView.setViewName("/pages/directories/company-statuses/index");
        modelAndView.addObject("companyStatuses", companyStatuses);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyStatusCreate')")
    public RedirectView createCompanyStatus(@RequestParam(name = "nameUz") String nameUz,
                                            @RequestParam(name = "nameRu") String nameRu,
                                            @RequestParam(name = "nameEn") String nameEn,
                                            @RequestParam(name = "description") String description,
                                            RedirectAttributes redirectAttributes) {
        CompanyStatus companyStatus = new CompanyStatus();
        companyStatus.setNameRu(nameRu);
        companyStatus.setNameUz(nameUz);
        companyStatus.setNameEn(nameEn);
        companyStatus.setDescription(description);
        companyStatusService.saveCompanyStatus(companyStatus);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/company-statuses/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyStatusUpdate')")
    public RedirectView updateCompanyStatus(@RequestParam(name = "id") Long id,
                                            @RequestParam(name = "nameUzEdit") String nameUz,
                                            @RequestParam(name = "nameRuEdit") String nameRu,
                                            @RequestParam(name = "nameEnEdit") String nameEn,
                                            @RequestParam(name = "descriptionEdit") String description,
                                            RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        CompanyStatus companyStatus = companyStatusService.getCompanyStatusById(id);
        companyStatus.setId(id);
        companyStatus.setNameRu(nameRu);
        companyStatus.setNameUz(nameUz);
        companyStatus.setNameEn(nameEn);
        companyStatus.setDescription(description);
        companyStatusService.saveCompanyStatus(companyStatus);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/company-statuses/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyStatusDelete')")
    public RedirectView deleteCompanyStatus(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        companyStatusService.deleteCompanyStatus(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/company-statuses/");
    }
}
