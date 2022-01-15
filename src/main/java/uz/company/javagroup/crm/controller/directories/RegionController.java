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
import uz.isd.javagroup.grandcrm.entity.directories.Region;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.AreaService;
import uz.isd.javagroup.grandcrm.services.directories.RegionService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("directory/regions")
public class RegionController extends BaseController {

    @Autowired
    RegionService regionService;

    @Autowired
    AreaService areaService;

    @RequestMapping(path = {"/", "/{areaId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('RegionRead')")
    public ModelAndView index(@PathVariable(name = "areaId") Optional<Long> areaId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Region> regions;
        if (areaId.isPresent()) regions = regionService.byAreaId(areaId.get());
        else regions = regionService.findAll();
        List<Area> areas = areaService.findAll();
        modelAndView.setViewName("/pages/directories/regions/index");
        modelAndView.addObject("regions", regions);
        modelAndView.addObject("areas", areas);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('RegionCreate')")
    public RedirectView createRegion(@RequestParam(name = "areaId") Long areaId,
                                     @RequestParam(name = "nameUz") String nameUz,
                                     @RequestParam(name = "nameRu") String nameRu,
                                     @RequestParam(name = "nameEn") String nameEn,
                                     RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Region region = new Region();
        region.setNameUz(nameUz);
        region.setNameRu(nameRu);
        region.setNameEn(nameEn);
        region.setArea(areaService.getAreaById(areaId));
        regionService.saveRegion(region);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/regions/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('RegionUpdate')")
    public RedirectView updateRegion(@RequestParam(name = "id") Long id,
                                     @RequestParam(name = "areaEdit") Long areaId,
                                     @RequestParam(name = "nameUzEdit") String nameUz,
                                     @RequestParam(name = "nameRuEdit") String nameRu,
                                     @RequestParam(name = "nameEnEdit") String nameEn,
                                     RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Region region = regionService.getRegionById(id);
        region.setId(id);
        region.setNameUz(nameUz);
        region.setNameRu(nameRu);
        region.setNameEn(nameEn);
        region.setArea(areaService.getAreaById(areaId));
        regionService.saveRegion(region);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/regions/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('RegionDelete')")
    public RedirectView deleteRegion(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        regionService.deleteRegion(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/regions/");
    }
}
