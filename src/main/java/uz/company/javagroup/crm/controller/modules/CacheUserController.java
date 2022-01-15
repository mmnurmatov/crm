package uz.isd.javagroup.grandcrm.controller.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.entity.modules.CacheUser;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.RoleService;
import uz.isd.javagroup.grandcrm.services.modules.CacheService;
import uz.isd.javagroup.grandcrm.services.modules.CacheUserService;
import uz.isd.javagroup.grandcrm.services.modules.UserService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("module/cache-users")
public class CacheUserController extends BaseController {

    @Autowired
    CacheUserService cacheUserService;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    CacheService cacheService;

    @RequestMapping(path = { "/{cacheId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('CacheUserRead')")
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
        List<CacheUser> cacheUsers;
        if (cacheId.isPresent()) cacheUsers = cacheUserService.findAllByCacheId(cacheId.get());
        else cacheUsers = cacheUserService.findAll();

        Role role = roleService.findByCode("Cache");

        Cache cache = cacheService.getCacheById(cacheId.get());

        List<User> users = userService.getUsersByCompanyIdAndRoleId(cache.getCompany().getId(), role.getId());

        modelAndView.addObject("cacheUsers", cacheUsers);
        modelAndView.addObject("users", users);
        modelAndView.addObject("cache", cache);

        modelAndView.setViewName("/pages/modules/cache-users/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CacheUserCreate')")
    public RedirectView createArea(@RequestParam(name = "userId") Optional<Long> userId,
                                   @RequestParam(name = "cacheId") Optional<Long> cacheId,
                                   @RequestParam(name = "status") int status,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        CacheUser cacheUser = new CacheUser();
        cacheUser.setStatus(status);
        cacheUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        if (userId.isPresent()){
            cacheUser.setUser(userService.getUserById(userId.get()));
        }else {
            throw new RecordNotFoundException("User is not found with Id" + userId);
        }

        if (cacheId.isPresent()){
            cacheUser.setCache(cacheService.getCacheById(cacheId.get()));
        }else {
            throw new RecordNotFoundException("Cache is not found with Id" + cacheId);
        }

        cacheUserService.saveCacheUser(cacheUser);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/module/cache-users/" + cacheId.get());
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('CacheUserUpdate')")
    public RedirectView updateArea(@RequestParam(name = "id") Long id,
                                   @RequestParam(name = "userEdit") Optional<Long> userId,
                                   @RequestParam(name = "cacheEdit") Optional<Long> cacheId,
                                   @RequestParam(name = "statusEdit") int status,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {

        CacheUser cacheUser = cacheUserService.getCacheUserById(id);

        cacheUser.setId(id);
        cacheUser.setStatus(status);
        cacheUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        if (userId.isPresent()){
            cacheUser.setUser(userService.getUserById(userId.get()));
        }else {
            throw new RecordNotFoundException("User is not found with Id" + userId);
        }

        if (cacheId.isPresent()){
            cacheUser.setCache(cacheService.getCacheById(cacheId.get()));
        }else {
            throw new RecordNotFoundException("Cache is not found with Id" + cacheId);
        }

        cacheUserService.saveCacheUser(cacheUser);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/cache-users/" + cacheId.get());
    }

}
