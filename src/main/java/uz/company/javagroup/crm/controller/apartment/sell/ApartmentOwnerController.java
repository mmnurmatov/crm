package uz.isd.javagroup.grandcrm.controller.apartment.sell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwner;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.apartment.sell.ApartmentOwnerService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("apartment-sell/apartment_owners")
public class ApartmentOwnerController extends BaseController {

    @Autowired
    ApartmentOwnerService apartmentOwnerService;


    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ApartmentOwnersRead')")
    public ModelAndView index(@ModelAttribute("status") String status,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {

        if (status.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", status);
            modelAndView.addObject("message", messageData);
        }

        List<ApartmentOwner> apartmentOwners = apartmentOwnerService.findAll();

        modelAndView.addObject("apartmentOwners", apartmentOwners);

        modelAndView.setViewName("/pages/apartment-sells/apartment-owners/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ApartmentOwnersCreate')")
    public RedirectView createApartmentOwners(
                                              @RequestParam(name = "fullName") String fullName,
                                              @RequestParam(name = "passportData") String passportData,
                                              RedirectAttributes redirectAttributes) throws RecordNotFoundException, IOException {

        ApartmentOwner apartmentOwners = new ApartmentOwner();
        apartmentOwners.setFullName(fullName);
        apartmentOwners.setPassportData(passportData);
        apartmentOwners.setCreatedAt(new Date());
        apartmentOwnerService.saveApartmentOwner(apartmentOwners);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/apartment-sell/apartment_owners/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ApartmentOwnersUpdate')")
    public RedirectView updateApartmentOwners(
                                         @RequestParam(name = "id") Long id,
                                         @RequestParam(name = "fullNameEdit") String fullName,
                                         @RequestParam(name = "passportDataEdit") String passportData,
                                         RedirectAttributes redirectAttributes) throws RecordNotFoundException, IOException {

        ApartmentOwner apartmentOwner = apartmentOwnerService.getApartmentOwnerById(id);
        apartmentOwner.setId(id);
        apartmentOwner.setFullName(fullName);
        apartmentOwner.setPassportData(passportData);
        apartmentOwner.setUpdatedAt(new Date());

        apartmentOwnerService.saveApartmentOwner(apartmentOwner);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/apartment-sell/apartment_owners/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ApartmentOwnersDelete')")
    public RedirectView deleteApartmentOwners(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {

        apartmentOwnerService.deleteApartmentOwner(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");

        return new RedirectView("/apartment-sell/apartment_owners/");
    }

}
