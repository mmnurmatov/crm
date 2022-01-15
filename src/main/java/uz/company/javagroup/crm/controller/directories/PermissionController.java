package uz.isd.javagroup.grandcrm.controller.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import uz.isd.javagroup.grandcrm.controller.BaseController;
import uz.isd.javagroup.grandcrm.entity.directories.Permission;
import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.PermissionService;
import uz.isd.javagroup.grandcrm.services.directories.RoleService;

import java.util.*;

@Controller
@RequestMapping("directory/permissions")
public class PermissionController extends BaseController {

    @Autowired
    PermissionService permissionService;
    @Autowired
    RoleService roleService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('PermissionRead')")
    public ModelAndView index(
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) throws RecordNotFoundException {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }

        List<Permission> permissions = permissionService.findAll(Sort.by("type").descending().and(Sort.by("id").ascending()));

        modelAndView.addObject("permissions", permissions);
        modelAndView.setViewName("/pages/directories/permissions/index");
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('PermissionCreate')")
    public RedirectView createPermission(@RequestParam(name = "name") String name,
                                         @RequestParam(name = "type") String type,
                                         @RequestParam(name = "isModule") Boolean isModule,
                                         RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setType(type);
        permission.setIsModule(isModule);
        permissionService.savePermission(permission);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Yangi ma'lumot muvvaffaqiyatli qo'shildi");
        return new RedirectView("/directory/permissions/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('PermissionUpdate')")
    public RedirectView updatePermission(@RequestParam(name = "id") Long id,
                                         @RequestParam(name = "nameEdit") String name,
                                         @RequestParam(name = "typeEdit") String type,
                                         @RequestParam(name = "isModuleEdit") Boolean isModule,
                                         RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        Permission permission = permissionService.getPermissionById(id);
        permission.setId(id);
        permission.setName(name);
        permission.setType(type);
        permission.setIsModule(isModule);
        permissionService.savePermission(permission);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/directory/permissions/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('PermissionDelete')")
    public RedirectView deletePermission(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        permissionService.deletePermission(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/directory/permissions/");
    }
}
