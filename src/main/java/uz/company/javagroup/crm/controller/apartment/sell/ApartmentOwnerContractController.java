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
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwnerContract;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.apartment.sell.ApartmentOwnerContractService;
import uz.isd.javagroup.grandcrm.services.apartment.sell.ApartmentOwnerService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Controller
@RequestMapping("apartment-sell/apartment_owner_contracts")
public class ApartmentOwnerContractController extends BaseController {

    @Autowired
    ApartmentOwnerContractService apartmentOwnerContractService;

    @Autowired
    ApartmentOwnerService apartmentOwnerService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ApartmentOwnerContractRead')")
    public ModelAndView index(ModelAndView modelAndView,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<ApartmentOwnerContract> apartmentOwnerContractList = apartmentOwnerContractService.findAll();
        List<ApartmentOwner> apartmentOwnerList = apartmentOwnerService.findAll();

        modelAndView.addObject("apartmentOwnerList", apartmentOwnerList);
        modelAndView.addObject("apartmentOwnerContractList", apartmentOwnerContractList);

        modelAndView.setViewName("/pages/apartment-sells/apartment-owner-contracts/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ApartmentOwnerContractCreate')")
    public RedirectView createApartmentOwnerContract(

                                             @RequestParam(name = "apartmentOwnerId") Optional<Long> apartmentOwnerId,
                                             @RequestParam(name = "contractNumber") String contractNumber,
                                             @RequestParam(name = "floor") int floor,
                                             @RequestParam(name = "roomCount") int roomCount,
                                             @RequestParam(name = "apartmentArea") BigDecimal apartmentArea,
                                             @RequestParam(name = "contractSum") BigDecimal contractSum,
                                             @RequestParam(name = "firstPaySum") BigDecimal firstPaySum,
                                             @RequestParam(name = "firstPayPercent") int firstPayPercent,
                                             @RequestParam(name = "apartmentCreditPaymentMonths") int apartmentCreditPaymentMonths,

            RedirectAttributes redirectAttributes) throws IOException, RecordNotFoundException {

        ApartmentOwnerContract apartmentOwnerContract = new ApartmentOwnerContract();

        if(apartmentOwnerId.isPresent()){
            apartmentOwnerContract.setApartmentOwner(apartmentOwnerService.getApartmentOwnerById(apartmentOwnerId.get()));
        }

        apartmentOwnerContract.setContractNumber(contractNumber);
        apartmentOwnerContract.setFloor(floor);
        apartmentOwnerContract.setRoomCount(roomCount);
        apartmentOwnerContract.setApartmentArea(apartmentArea);
        apartmentOwnerContract.setContractSum(contractSum);
        apartmentOwnerContract.setFirstPayPercent(firstPayPercent);
        apartmentOwnerContract.setFirstPaySum(firstPaySum);
        apartmentOwnerContract.setApartmentCreditPaymentMonths(apartmentCreditPaymentMonths);
        apartmentOwnerContract.setCreatedAt(new Date());

        apartmentOwnerContractService.saveApartmentOwnerContract(apartmentOwnerContract);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/apartment-sell/apartment_owner_contracts/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ApartmentOwnerContractUpdate')")
    public RedirectView updateApartmentOwnerContract(

                                                     @RequestParam(name = "id") Optional<Long> id,
                                                     @RequestParam(name = "apartmentOwnerIdEdit") Optional<Long> apartmentOwnerId,
                                                     @RequestParam(name = "contractNumberEdit") String contractNumber,
                                                     @RequestParam(name = "floorEdit") int floor,
                                                     @RequestParam(name = "roomCountEdit") int roomCount,
                                                     @RequestParam(name = "apartmentAreaEdit") BigDecimal apartmentArea,
                                                     @RequestParam(name = "contractSumEdit") BigDecimal contractSum,
                                                     @RequestParam(name = "firstPaySumEdit") BigDecimal firstPaySum,
                                                     @RequestParam(name = "firstPayPercentEdit") int firstPayPercent,
                                                     @RequestParam(name = "apartmentCreditPaymentMonthsEdit") int apartmentCreditPaymentMonths,
                                                     RedirectAttributes redirectAttributes) throws RecordNotFoundException, IOException {

        if(id.isPresent()){
            ApartmentOwnerContract apartmentOwnerContract = apartmentOwnerContractService.getApartmentOwnerContractById(id.get());

            apartmentOwnerContract.setId(id.get());

            if(apartmentOwnerId.isPresent()){
                apartmentOwnerContract.setApartmentOwner(apartmentOwnerService.getApartmentOwnerById(apartmentOwnerId.get()));
            }else {
                redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
                redirectAttributes.addFlashAttribute("message", "Ma'lumot topilmadi?");
                return new RedirectView("/apartment-sell/apartment_owner_contracts/");
            }

            apartmentOwnerContract.setContractNumber(contractNumber);
            apartmentOwnerContract.setFloor(floor);
            apartmentOwnerContract.setRoomCount(roomCount);
            apartmentOwnerContract.setApartmentArea(apartmentArea);
            apartmentOwnerContract.setContractSum(contractSum);
            apartmentOwnerContract.setFirstPayPercent(firstPayPercent);
            apartmentOwnerContract.setFirstPaySum(firstPaySum);
            apartmentOwnerContract.setApartmentCreditPaymentMonths(apartmentCreditPaymentMonths);
            apartmentOwnerContract.setUpdatedAt(new Date());
            apartmentOwnerContractService.saveApartmentOwnerContract(apartmentOwnerContract);

        }else {
            redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
            redirectAttributes.addFlashAttribute("message", "Ma'lumot topilmadi?");
            return new RedirectView("/apartment-sell/apartment_owner_contracts/");
        }


        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/apartment-sell/apartment_owner_contracts/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ApartmentOwnerContractDelete')")
    public RedirectView deleteApartmentOwnerContract( @RequestParam(name = "rowId") Long id,
                                                      RedirectAttributes redirectAttributes) {

        apartmentOwnerContractService.deleteApartmentOwnerContract(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");

        return new RedirectView("/apartment-sell/apartment_owner_contracts/");
    }


//     Print Payment Months
    @RequestMapping(path = "/pdf/{apartmentContractId}", method = RequestMethod.GET)
    public ModelAndView createPDF(@PathVariable(name = "apartmentContractId") Optional<Long> apartmentContractId,
                                  ModelAndView modelAndView) throws RecordNotFoundException {

        ApartmentOwnerContract apartmentOwnerContract = apartmentOwnerContractService.getApartmentOwnerContractById(apartmentContractId.get());
        List<String> apartmentCreditSumsAmongMonths = new ArrayList<>();
        LocalDate months = LocalDate.now();
        double sum = apartmentOwnerContract.getContractSum().doubleValue();
        double percent = apartmentOwnerContract.getFirstPayPercent();
        int month = apartmentOwnerContract.getApartmentCreditPaymentMonths();
        double total = sum - ((sum / 100) * percent);
        double everyMonth = total / month;
        int count = 1;

        while (count <= month) {

            if (months.getMonth().plus(count).equals(Month.MARCH)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.APRIL)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.MAY)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.JUNE)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.JULY)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.AUGUST)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.SEPTEMBER)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.OCTOBER)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.NOVEMBER)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.DECEMBER)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.JANUARY)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }
            if (months.getMonth().plus(count).equals(Month.FEBRUARY)) {
                apartmentCreditSumsAmongMonths.add(String.valueOf(months.getMonth().plus(count)));
            }

            count++;
        }

        modelAndView.addObject("apartmentCreditSumsAmongMonths", apartmentCreditSumsAmongMonths);
        modelAndView.addObject("apartmentOwnerContract", apartmentOwnerContract);
        modelAndView.addObject("total", total);
        modelAndView.addObject("everyMonth", everyMonth);

        modelAndView.setViewName("/pages/reports/apartment-selling-reports/invoice");
        return modelAndView;
    }
}
