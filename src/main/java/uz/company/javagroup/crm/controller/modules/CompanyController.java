package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.CompanyStatus;
import uz.isd.javagroup.grandcrm.entity.directories.CompanyType;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.Warehouse;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.CompanyStatusService;
import uz.isd.javagroup.grandcrm.services.directories.CompanyTypeService;
import uz.isd.javagroup.grandcrm.services.modules.CompanyService;
import uz.isd.javagroup.grandcrm.services.modules.WarehouseService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("module/company")
public class CompanyController extends BaseController {

    @Autowired
    CompanyService companyService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    CompanyTypeService companyTypeService;
    @Autowired
    CompanyStatusService companyStatusService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CompanyRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Company> companies = companyService.findAll();
        List<CompanyStatus> companyStatuses = companyStatusService.findAll();
        List<CompanyType> companyTypes = companyTypeService.findAll();
        modelAndView.setViewName("/pages/modules/companies/index");
        modelAndView.addObject("companies", companies);
        modelAndView.addObject("companyStatuses", companyStatuses);
        modelAndView.addObject("companyTypes", companyTypes);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyCreate')")
    public RedirectView createCompany(@RequestParam(name = "parent") Long parentId,
                                      @RequestParam(name = "companyState") Long companyStatusId,
                                      @RequestParam(name = "companyType") Long companyTypeId,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "address") String address,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        if (parentId != 0) {
            company.setParent(companyService.getCompanyById(parentId));
        }
        company.setCompanyStatus(companyStatusService.getCompanyStatusById(companyStatusId));
        company.setCompanyType(companyTypeService.getCompanyTypeById(companyTypeId));
        companyService.saveCompany(company);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/company/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyUpdate')")
    public RedirectView updateCompany(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "parentId") Optional<Long> parentId,
                                      @RequestParam(name = "companyStatusId") Long companyStatusId,
                                      @RequestParam(name = "companyTypeId") Long companyTypeId,
                                      @RequestParam(name = "nameEdit") String name,
                                      @RequestParam(name = "addressEdit") String address,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Company company = companyService.getCompanyById(id);
        company.setId(id);
        company.setName(name);
        company.setAddress(address);
        if (parentId.isPresent()) {
            company.setParent(companyService.getCompanyById(parentId.get()));
        }
        company.setCompanyStatus(companyStatusService.getCompanyStatusById(companyStatusId));
        company.setCompanyType(companyTypeService.getCompanyTypeById(companyTypeId));
        companyService.saveCompany(company);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/company/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CompanyDelete')")
    public RedirectView deleteCompany(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        companyService.deleteCompany(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/company/");
    }

    @RequestMapping(path = "/warehousesById/{companyId}", method = RequestMethod.GET)
    public ModelAndView companyWarehouses(@PathVariable(name = "companyId") Long companyId, ModelAndView modelAndView) throws RecordNotFoundException {
        Company company = companyService.getCompanyById(companyId);
        List<Warehouse> warehouses = warehouseService.findAllWarehousesByCompanyId(companyId);
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("company", company);
        modelAndView.setViewName("/pages/modules/companies/warehouses");
        return modelAndView;
    }
}
