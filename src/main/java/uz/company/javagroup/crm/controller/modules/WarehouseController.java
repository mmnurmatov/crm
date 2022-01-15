package uz.isd.javagroup.grandcrm.controller.modules;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.DivTagWorker;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.node.IElementNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.controller.directories.ProductController;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.entity.directories.Product;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("module/warehouse")
public class WarehouseController extends BaseController {

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

    private final TemplateEngine templateEngine;

    public WarehouseController(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('WarehouseRead')")
    public ModelAndView index(ModelAndView modelAndView, @ModelAttribute("status") String typesData, @ModelAttribute("message") String messageData) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        User user = this.getAuthUserData();
        List<Warehouse> warehouses;

        if(user.getRole().getCode().equals("DIRECTOR") || user.getRole().getCode().equals("SYS_ADMIN") || user.getRole().getCode().equals("ACCOUNTANT")){
             warehouses = warehouseService.findAll();
        }else {
             warehouses = Utils.ownWarehouses(this.getAuthUserId(), warehouseUserService);
        }
        List<Company> companies = companyService.findAll();
        List<Country> countries = countryService.findAll();
        modelAndView.addObject("warehouses", warehouses);
        modelAndView.addObject("companies", companies);
        modelAndView.addObject("countries", countries);
        modelAndView.addObject("regions", null);
        modelAndView.addObject("areas", null);
        modelAndView.setViewName("/pages/modules/warehouses/index");
        return modelAndView;
    }

    @RequestMapping(path = "/balance/{warehouseId}", method = RequestMethod.GET)
//    @PreAuthorize("hasAuthority('WarehouseBalance')")
    public ModelAndView createWarehouse(@PathVariable(name = "warehouseId") Long warehouseId, ModelAndView modelAndView) throws RecordNotFoundException {
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        modelAndView.addObject("warehouse", warehouse);
         modelAndView.addObject("balanceList", itemStatusService.warehouseBalance(warehouseId));
        modelAndView.setViewName("/pages/modules/warehouses/balance");
        return modelAndView;
    }


    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseCreate')")
    public RedirectView createWarehouse(@RequestParam(name = "companyId") Long companyId,
                                        @RequestParam(name = "regionId") Long regionId,
                                        @RequestParam(name = "name") String name,
                                        @RequestParam(name = "status")  Optional<Integer> status, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Warehouse warehouse = new Warehouse();
        if (companyId != 0) warehouse.setCompany(companyService.getCompanyById(companyId));
        if (regionId != 0) warehouse.setRegion(regionService.getRegionById(regionId));
        warehouse.setName(name);
        if(status.isPresent()) warehouse.setStatus(status.get());
        warehouse.setCreatedAt(new Date());
        warehouseService.saveWarehouse(warehouse);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/warehouse/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseUpdate')")
    public RedirectView updateWarehouse(@RequestParam(name = "id") Long id,
                                        @RequestParam(name = "companyId") Optional<Long> companyId,
                                        @RequestParam(name = "regionId") Optional<Long> regionId,
                                        @RequestParam(name = "nameEdit") String name,
                                        @RequestParam(name = "statusEdit") Optional<Integer> status, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        if (companyId.isPresent() && companyId.get() != -1) warehouse.setCompany(companyService.getCompanyById(companyId.get()));
        if (regionId.isPresent() && regionId.get() != -1) warehouse.setRegion(regionService.getRegionById(regionId.get()));
        warehouse.setName(name);
        if (status.isPresent())  warehouse.setStatus(status.get());
        warehouse.setUpdatedAt(new Date());
        warehouseService.saveWarehouse(warehouse);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/warehouse/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('WarehouseDelete')")
    public RedirectView deleteWarehouse(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        warehouseService.deleteWarehouse(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/warehouse/");
    }
}
