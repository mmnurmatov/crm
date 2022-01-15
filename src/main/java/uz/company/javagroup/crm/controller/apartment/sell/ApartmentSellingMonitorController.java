package uz.isd.javagroup.grandcrm.controller.apartment.sell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwner;
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwnerContract;
import uz.isd.javagroup.grandcrm.services.apartment.sell.ApartmentOwnerContractService;
import uz.isd.javagroup.grandcrm.services.apartment.sell.ApartmentOwnerService;

import java.util.List;

@Controller
@RequestMapping("apartment-sell/apartment_selling")
public class ApartmentSellingMonitorController extends BaseController {

    @Autowired
    ApartmentOwnerService apartmentOwnerService;

    @Autowired
    ApartmentOwnerContractService apartmentOwnerContractService;


    @RequestMapping(path = {"/"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ApartmentSellingMonitorRead')")
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
        List<ApartmentOwnerContract> apartmentOwnerContracts = apartmentOwnerContractService.findAll();

        modelAndView.addObject("apartmentOwners", apartmentOwners);
        modelAndView.addObject("apartmentOwnerContracts", apartmentOwnerContracts);

        modelAndView.setViewName("/pages/apartment-sells/apartment-selling/index");
        return modelAndView;
    }

}
