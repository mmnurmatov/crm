package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.CountryService;
import uz.isd.javagroup.grandcrm.services.directories.RegionService;
import uz.isd.javagroup.grandcrm.services.modules.CompanyService;
import uz.isd.javagroup.grandcrm.services.modules.ItemStatusService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseUserService;
import uz.isd.javagroup.grandcrm.utility.Utils;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;

@Controller
@RequestMapping("module/production")
public class ProductionController extends BaseController {

    @Autowired
    WarehouseService warehouseService;
    @Autowired
    WarehouseUserService warehouseUserService;
    @Autowired
    CompanyService companyService;
    @Autowired
    CountryService countryService;
    @Autowired
    RegionService regionService;
    @Autowired
    ItemStatusService itemStatusService;
    @Autowired
    ServletContext servletContext;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ProductionRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) throws RecordNotFoundException {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        User user = this.getAuthUserData();
        List<Warehouse> productions;

        if (user.getRole().getCode().equals("DIRECTOR") || user.getRole().getCode().equals("SYS_ADMIN") || user.getRole().getCode().equals("ADMIN")) {
            productions = warehouseService.findAllProductions();
        } else {
            productions = Utils.ownProductions(this.getAuthUserId(), warehouseUserService);
        }
        List<Company> companies = companyService.findAll();
        List<Country> countries = countryService.findAll();
        modelAndView.addObject("productions", productions);
        modelAndView.addObject("companies", companies);
        modelAndView.addObject("countries", countries);
        modelAndView.addObject("regions", null);
        modelAndView.addObject("areas", null);
        modelAndView.setViewName("/pages/modules/productions/index");
        return modelAndView;
    }

    @RequestMapping(path = "/balance/{productionId}", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('WarehouseBalance')")
    public ModelAndView createWarehouse(@PathVariable(name = "productionId") Long productionId, ModelAndView modelAndView) throws RecordNotFoundException {
        Warehouse warehouse = warehouseService.getWarehouseById(productionId);
        modelAndView.addObject("warehouse", warehouse);
        modelAndView.addObject("balanceList", itemStatusService.warehouseBalance(productionId));
        modelAndView.setViewName("/pages/modules/productions/balance");
        return modelAndView;
    }


    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductionCreate')")
    public RedirectView createProduction(@RequestParam(name = "companyId") Long companyId,
                                         @RequestParam(name = "regionId") Long regionId,
                                         @RequestParam(name = "name") String name,
                                         @RequestParam(name = "status") Optional<Integer> status, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Warehouse warehouse = new Warehouse();
        if (companyId != 0) warehouse.setCompany(companyService.getCompanyById(companyId));
        if (regionId != 0) warehouse.setRegion(regionService.getRegionById(regionId));
        warehouse.setName(name);
        status.ifPresent(warehouse::setStatus);
        warehouse.setCreatedAt(new Date());
        warehouse.setIsProduction(TRUE);
        warehouseService.saveWarehouse(warehouse);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/production/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductionUpdate')")
    public RedirectView updateProduction(@RequestParam(name = "id") Long id,
                                         @RequestParam(name = "companyId") Optional<Long> companyId,
                                         @RequestParam(name = "regionId") Optional<Long> regionId,
                                         @RequestParam(name = "nameEdit") String name,
                                         @RequestParam(name = "statusEdit") Optional<Integer> status, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        if (companyId.isPresent() && companyId.get() != -1)
            warehouse.setCompany(companyService.getCompanyById(companyId.get()));
        if (regionId.isPresent() && regionId.get() != -1)
            warehouse.setRegion(regionService.getRegionById(regionId.get()));
        warehouse.setName(name);
        status.ifPresent(warehouse::setStatus);
        warehouse.setUpdatedAt(new Date());
        warehouse.setIsProduction(TRUE);
        warehouseService.saveWarehouse(warehouse);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/production/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ProductionDelete')")
    public RedirectView deleteProduction(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        warehouseService.deleteWarehouse(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/production/");
    }
}
