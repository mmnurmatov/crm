package uz.isd.javagroup.grandcrm.controller.modules;

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
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.entity.directories.Region;
import uz.isd.javagroup.grandcrm.entity.modules.Contragent;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.AreaService;
import uz.isd.javagroup.grandcrm.services.directories.CountryService;
import uz.isd.javagroup.grandcrm.services.directories.RegionService;
import uz.isd.javagroup.grandcrm.services.modules.ContragentService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("module/contragents")
public class ContragentController extends BaseController {

    @Autowired
    ContragentService contragentService;
    @Autowired
    CountryService countryService;
    @Autowired
    AreaService areaService;
    @Autowired
    RegionService regionService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ContragentRead')")
    public ModelAndView index(@ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData, ModelAndView modelAndView) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<Contragent> contragents = contragentService.findAll();
        List<Country> countries = countryService.findAll();
        List<Area> areas = areaService.findAll();
        List<Region> regions = regionService.findAll();

        modelAndView.addObject("contragents", contragents);
        modelAndView.addObject("countries", countries);
        modelAndView.addObject("areas", areas);
        modelAndView.addObject("regions", regions);

        modelAndView.setViewName("/pages/modules/contragents/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ContragentCreate')")
    public RedirectView createContragent(
                                         @RequestParam(name = "regionId") Long regionId,
                                         @RequestParam(name = "fullName") String fullName,
                                         @RequestParam(name = "shortName") String shortName,
                                         @RequestParam(name = "phone") String phone,
                                         @RequestParam(name = "mobile") String mobile,
                                         @RequestParam(name = "email") String email,
                                         @RequestParam(name = "address") String address,
                                         @RequestParam(name = "address2") String address2,
                                         @RequestParam(name = "inn") String inn,
                                         @RequestParam(name = "okonx") String okonx,
                                         @RequestParam(name = "bankAccount") String bankAccount,
                                         @RequestParam(name = "vatAccount") String vatAccount,
                                         RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Contragent contragent = new Contragent();
        if (regionId != null) contragent.setRegion(regionService.getRegionById(regionId));
        contragent.setFullName(fullName);
        contragent.setShortName(shortName);
        contragent.setPhone(phone);
        contragent.setMobile(mobile);
        contragent.setEmail(email);
        contragent.setAddress(address);
        contragent.setAddress2(address2);
        contragent.setInn(inn);
        contragent.setOkonx(okonx);
        contragent.setBankAccount(bankAccount);
        contragent.setVatAccount(vatAccount);
        contragentService.saveContragent(contragent);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/contragents/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ContragentUpdate')")
    public RedirectView updateContragent(@RequestParam(name = "id") Long id,
                                         @RequestParam(name = "region") Optional<Long> regionId,
                                         @RequestParam(name = "fullNameEdit") String fullName,
                                         @RequestParam(name = "shortNameEdit") String shortName,
                                         @RequestParam(name = "phoneEdit") String phone,
                                         @RequestParam(name = "mobileEdit") String mobile,
                                         @RequestParam(name = "emailEdit") String email,
                                         @RequestParam(name = "addressEdit") String address,
                                         @RequestParam(name = "address2Edit") String address2,
                                         @RequestParam(name = "innEdit") String inn,
                                         @RequestParam(name = "okonxEdit") String okonx,
                                         @RequestParam(name = "bankAccountEdit") String bankAccount,
                                         @RequestParam(name = "vatAccountEdit") String vatAccount,
                                         RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Contragent contragent = contragentService.getContragentById(id);
        if (regionId.isPresent() && regionId.get() != -1) contragent.setRegion(regionService.getRegionById(regionId.get()));
        contragent.setFullName(fullName);
        contragent.setShortName(shortName);
        contragent.setPhone(phone);
        contragent.setMobile(mobile);
        contragent.setEmail(email);
        contragent.setAddress(address);
        contragent.setAddress2(address2);
        contragent.setInn(inn);
        contragent.setOkonx(okonx);
        contragent.setBankAccount(bankAccount);
        contragent.setVatAccount(vatAccount);
        contragentService.saveContragent(contragent);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/contragents/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ContragentDelete')")
    public RedirectView deleteContragent(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        contragentService.deleteContragent(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/contragents/");
    }

}
