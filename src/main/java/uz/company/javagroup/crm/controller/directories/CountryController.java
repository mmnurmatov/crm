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
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.CountryService;

import java.util.List;

@Controller
@RequestMapping("directory/country")
public class CountryController extends BaseController {

    @Autowired
    CountryService countryService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CountryRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Country> countries = countryService.findAll();
        modelAndView.setViewName("/pages/directories/countries/index");
        modelAndView.addObject("countries", countries);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CountryCreate')")
    public RedirectView createCountry(@RequestParam(name = "nameUz") String nameUz,
                                      @RequestParam(name = "nameRu") String nameRu,
                                      @RequestParam(name = "nameEn") String nameEn,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Country country = new Country();
        country.setNameUz(nameUz);
        country.setNameRu(nameRu);
        country.setNameEn(nameEn);
        countryService.saveCountry(country);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/country/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CountryUpdate')")
    public RedirectView updateCountry(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "nameUzEdit") String nameUz,
                                      @RequestParam(name = "nameRuEdit") String nameRu,
                                      @RequestParam(name = "nameEnEdit") String nameEn,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Country country = countryService.getCountryById(id);
        country.setId(id);
        country.setNameUz(nameUz);
        country.setNameRu(nameRu);
        country.setNameEn(nameEn);
        countryService.saveCountry(country);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/country/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CountryDelete')")
    public RedirectView deleteCountry(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        countryService.deleteCountry(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/country/");
    }
}
