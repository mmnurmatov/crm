package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.modules.CacheBalanceData;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.modules.CacheBalanceDataService;
import uz.isd.javagroup.grandcrm.services.modules.CacheService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@Controller
@RequestMapping("module/cache-balance-data")
public class CacheBalanceDataController extends BaseController {

    @Autowired
    CacheBalanceDataService cacheBalanceDataService;

    @Autowired
    CacheService cacheService;

    @RequestMapping(path = { "/{cacheId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CacheBalanceDataRead')")
    public ModelAndView index(@PathVariable(name = "cacheId") Optional<Long> cacheId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        Optional<CacheBalanceData> cacheBalanceData = null;

        if (cacheId.isPresent()) {
            cacheBalanceData = cacheBalanceDataService.findByCacheId(cacheId.get());
            if(!cacheBalanceData.isPresent()){
                CacheBalanceData cacheBalanceDataNew = new CacheBalanceData();
                cacheBalanceDataNew.setCache(cacheService.getCacheById(cacheId.get()));
                cacheBalanceDataNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                cacheBalanceDataNew.setMoneyInCache(BigDecimal.ZERO);
                cacheBalanceDataNew.setMoneyInCard(BigDecimal.ZERO);
                cacheBalanceDataNew.setMoneyInTerminal(BigDecimal.ZERO);
                cacheBalanceDataNew.setMoneyInEnumeration(BigDecimal.ZERO);
                cacheBalanceDataService.saveCacheBalanceData(cacheBalanceDataNew);
                modelAndView.addObject("cacheBalanceData", cacheBalanceDataNew);
            }else {
                modelAndView.addObject("cacheBalanceData", cacheBalanceData.get());
            }
        }

        modelAndView.setViewName("/pages/modules/cache-balance-data/index");
        return modelAndView;
    }

}
