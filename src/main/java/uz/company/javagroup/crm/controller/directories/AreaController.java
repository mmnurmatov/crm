package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.AreaService;
import uz.isd.javagroup.grandcrm.services.directories.CountryService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("directory/areas")
public class   AreaController extends BaseController {

    @Autowired
    AreaService areaService;

    @Autowired
    CountryService countryService;

    @RequestMapping(path = {"/", "/{countryId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('AreaRead')")
    public ModelAndView index(@PathVariable(name = "countryId") Optional<Long> countryId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<Area> areas;
        if (countryId.isPresent()) areas = areaService.byCountryId(countryId.get());
        else areas = areaService.findAll();
        List<Country> countries = countryService.findAll();
        modelAndView.setViewName("/pages/directories/areas/index");
        modelAndView.addObject("countries", countries);
        modelAndView.addObject("areas", areas);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('AreaCreate')")
    public RedirectView createArea(@RequestParam(name = "countryId") Long countryId,
                                   @RequestParam(name = "nameUz") String nameUz,
                                   @RequestParam(name = "nameRu") String nameRu,
                                   @RequestParam(name = "nameEn") String nameEn,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Area area = new Area();
        area.setNameUz(nameUz);
        area.setNameRu(nameRu);
        area.setNameEn(nameEn);
        area.setCountry(countryService.getCountryById(countryId));
        areaService.saveArea(area);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/areas/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('AreaUpdate')")
    public RedirectView updateArea(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "countryEdit") Long countryId,
                                   @RequestParam(name = "nameUzEdit") String nameUz,
                                   @RequestParam(name = "nameRuEdit") String nameRu,
                                   @RequestParam(name = "nameEnEdit") String nameEn,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Area area = areaService.getAreaById(id);
        area.setId(id);
        area.setNameUz(nameUz);
        area.setNameRu(nameRu);
        area.setNameEn(nameEn);
        area.setCountry(countryService.getCountryById(countryId));
        areaService.saveArea(area);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/areas/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('AreaDelete')")
    public RedirectView deleteArea(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        areaService.deleteArea(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/areas/");
    }
}
