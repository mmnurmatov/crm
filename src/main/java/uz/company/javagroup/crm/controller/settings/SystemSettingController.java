package uz.isd.javagroup.grandcrm.controller.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.settings.SystemSetting;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.settings.SystemSettingsService;

import java.util.List;

@Controller
@RequestMapping("directory/system-setting")
public class SystemSettingController extends BaseController {

    @Autowired
    SystemSettingsService systemSettingsService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SystemSettingRead')")
    public ModelAndView index(
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {
        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<SystemSetting> systemSettings = systemSettingsService.findAll();
        modelAndView.setViewName("/pages/system-settings/index");
        modelAndView.addObject("systemSettings", systemSettings);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SystemSettingCreate')")
    public RedirectView createSystemSetting(
                                   @RequestParam(name = "key") String key,
                                   @RequestParam(name = "value") String value,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        SystemSetting systemSetting = new SystemSetting();
        systemSetting.setKey(key);
        systemSetting.setValue(value);
        systemSettingsService.saveSystemSetting(systemSetting);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/system-setting/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SystemSettingUpdate')")
    public RedirectView updateSystemSetting(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "keyEdit") String key,
                                   @RequestParam(name = "valueEdit") String value,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        SystemSetting systemSetting = new SystemSetting();
        systemSetting.setId(id);
        systemSetting.setKey(key);
        systemSetting.setValue(value);
        systemSettingsService.saveSystemSetting(systemSetting);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/system-setting/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SystemSettingDelete')")
    public RedirectView deleteSystemSetting(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        systemSettingsService.deleteSystemSetting(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/system-setting/");
    }

}
