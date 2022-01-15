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
import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.CacheRepository;
import uz.isd.javagroup.grandcrm.services.modules.CacheService;
import uz.isd.javagroup.grandcrm.services.modules.CompanyService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("module/cache")
public class CacheController extends BaseController {

    @Autowired
    CacheService cacheService;

    @Autowired
    CompanyService companyService;

    @Autowired
    CacheRepository cacheRepository;


    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CashRead')")
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

        User user = this.getAuthUserData();
        List<Cache> caches;
        int isCheck = 0;

        if(user.getRole().getCode().equals("ADMIN") || user.getRole().getCode().equals("SYS_ADMIN")){
            caches = cacheService.findAll();
            isCheck = 1;
        }else {
            caches = cacheService.getAll(user.getId());
        }

        List<Company> companies = companyService.findAll();

        modelAndView.addObject("isCheck", isCheck);
        modelAndView.addObject("caches", caches);
        modelAndView.addObject("companies", companies);

        modelAndView.setViewName("/pages/modules/caches/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CashCreate')")
    public RedirectView createCache(@RequestParam(name = "companyId") Long companyId,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "status") int status,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Cache cache = new Cache();

        cache.setName(name);
        cache.setStatus(status);
        cache.setDebet(BigDecimal.ZERO);
        cache.setKredit(BigDecimal.ZERO);
        cache.setBalance(BigDecimal.ZERO);
        cache.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        if (companyId != 0) {
            cache.setCompany(companyService.getCompanyById(companyId));
        }

        cacheService.saveCache(cache);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/cache/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CashUpdate')")
    public RedirectView updateCash(@RequestParam(name = "id") Long id,
                                      @RequestParam(name = "companyEditId") Long companyId,
                                      @RequestParam(name = "statusEdit") int status,
                                      @RequestParam(name = "nameEdit") String name,
                                      RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        Cache cache = cacheService.getCacheById(id);
        cache.setId(id);
        cache.setName(name);
        cache.setStatus(status);
        cache.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        if (companyId != 0) {
            cache.setCompany(companyService.getCompanyById(companyId));
        }

        cacheService.saveCache(cache);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/cache/");
    }

}
