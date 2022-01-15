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
import uz.isd.javagroup.grandcrm.entity.directories.UserStatus;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.services.directories.RoleService;
import uz.isd.javagroup.grandcrm.services.directories.UserStatusService;
import uz.isd.javagroup.grandcrm.services.modules.CompanyService;
import uz.isd.javagroup.grandcrm.services.modules.UserService;
import uz.isd.javagroup.grandcrm.utility.SecurityUtility;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("module/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserStatusService userStatusService;
    @Autowired
    CompanyService companyService;

    @RequestMapping(path = {"/", "/{companyId}"}, method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('UserRead')")
    public ModelAndView index(@PathVariable(name = "companyId") Optional<Long> companyId,
                              @ModelAttribute("status") String typesData,
                              @ModelAttribute("message") String messageData, ModelAndView modelAndView) {

        if (typesData.equals("")) {
            modelAndView.addObject("status", null);
            modelAndView.addObject("message", null);
        } else {
            modelAndView.addObject("status", typesData);
            modelAndView.addObject("message", messageData);
        }
        List<User> users;
        if (companyId.isPresent()) users = userService.byCompanyId(companyId.get());
        else users = userService.findAll();
        List<UserStatus> userStatuses = userStatusService.findAll();
        List<Role> roles = roleService.findAll();
        List<Company> companies = companyService.findAll();
        modelAndView.setViewName("/pages/modules/users/index");
        modelAndView.addObject("users", users);
        modelAndView.addObject("userStatuses", userStatuses);
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("companies", companies);
        return modelAndView;
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('UserCreate')")
    public RedirectView createUser(@RequestParam(name = "userStatus") Long userStatusId,
                                   @RequestParam(name = "role") Long roleId,
                                   @RequestParam(name = "company") Long companyId,
                                   @RequestParam(name = "username") String username,
                                   @RequestParam(name = "firstName") String firstName,
                                   @RequestParam(name = "secondName") String secondName,
                                   @RequestParam(name = "lastName") String lastName,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "phone") String phoneNumber, RedirectAttributes redirectAttributes) throws RecordNotFoundException, IOException {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            redirectAttributes.addFlashAttribute("status", this.getStatusError());
            redirectAttributes.addFlashAttribute("message", "User exist with username: " + username);
            return new RedirectView("/module/user/");
        }
        user = userService.getUserByEmail(email);
        if (user != null) {
            redirectAttributes.addFlashAttribute("status", this.getStatusError());
            redirectAttributes.addFlashAttribute("message", "User exist with email: " + email);
            return new RedirectView("/module/user/");
        }
        user = userService.getUserByPhoneNumber(phoneNumber);
        if (user != null) {
            redirectAttributes.addFlashAttribute("status", this.getStatusError());
            redirectAttributes.addFlashAttribute("message", "User exist with phoneNumber: " + phoneNumber);
            return new RedirectView("/module/user/");
        }
        user = new User();
        if (userStatusId != 0) user.setUserStatus(userStatusService.getUserStateByid(userStatusId));
        if (roleId != 0) user.setRole(roleService.getRoleById(roleId));
        if (companyId != 0) user.setCompany(companyService.getCompanyById(companyId));
        user.setUsername(username);
        user.setPassword(SecurityUtility.passwordEncoder().encode("12345678"));
        user.setFirstName(firstName);
        user.setSecondName(secondName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setImageUrl("/app-assets/images/default.png");
        user.setCreatedAt(new Date());
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userService.save(user);
//        this.sendEmailMessage(user.getUsername(), user.getEmail(), user.getToken());

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot qo'shildi va Email ga habar yuborildi...");
        return new RedirectView("/module/user/");
    }

    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('UserUpdate')")
    public RedirectView updateUser(@RequestParam(name = "id") Long id,
//                                   @RequestParam(name = "firstName") String firstName,
//                                   @RequestParam(name = "secondName") String secondName,
//                                   @RequestParam(name = "lastName") String lastName,
//                                   @RequestParam(name = "email") String email,
//                                   @RequestParam(name = "phoneNumber") String phoneNumber,
                                   @RequestParam(name = "companyEdit") Optional<Long> companyId,
                                   @RequestParam(name = "roleEdit") Optional<Long> roleId,
                                   @RequestParam(name = "userStatusEdit") Optional<Long> userStatusId,
                                   RedirectAttributes redirectAttributes) throws RecordNotFoundException {
//        User user = userService.getUserByEmail(email);
//        if (user != null) {
//            redirectAttributes.addFlashAttribute("status", this.getStatusError());
//            redirectAttributes.addFlashAttribute("message", "User exist with email: " + email);
//            return new RedirectView("/module/user/");
//        }
//        user = userService.getUserByPhoneNumber(phoneNumber);
//        if (user != null) {
//            redirectAttributes.addFlashAttribute("status", this.getStatusError());
//            redirectAttributes.addFlashAttribute("message", "User exist with phoneNumber: " + phoneNumber);
//            return new RedirectView("/module/user/");
//        }
//        user = userService.getUserById(id);
//        user.setFirstName(firstName);
//        user.setSecondName(secondName);
//        user.setLastName(lastName);
//        user.setPhoneNumber(phoneNumber);
//        userService.save(user);
//        String message = "";
//        if (!user.getEmail().equals(email)) {
//            String token = UUID.randomUUID().toString();
//            user.setToken(token);
//            user.setEmail(email);
//            userService.save(user);
//            this.sendEmailMessage(user.getUsername(), user.getEmail(), user.getToken());
//            message = " va Email ga habar yuborildi...";
//        }

        User user = userService.getUserById(id);
        if (userStatusId.isPresent()) user.setUserStatus(userStatusService.getUserStateByid(userStatusId.get()));
        if (roleId.isPresent()) user.setRole(roleService.getRoleById(roleId.get()));
        if (companyId.isPresent()) user.setCompany(companyService.getCompanyById(companyId.get()));
        userService.save(user);

        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot tahrirlandi");
        return new RedirectView("/module/user/");
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('UserDelete')")
    public RedirectView deleteUser(@RequestParam(name = "rowId") Long id, RedirectAttributes redirectAttributes) {
        companyService.deleteCompany(id);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Muvaffaqiyatli o'chirildi");
        return new RedirectView("/module/company/");
    }

    @RequestMapping(path = "/change-user-role", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('Change User Role')")
    public RedirectView changeUserRole(@RequestParam(name = "id") Long id, @RequestParam(name = "roleId") Long roleId, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        User user = userService.getUserById(id);
//        user.setRole(roleService.getRoleById(roleId));
        userService.save(user);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/user/");
    }

    @RequestMapping(path = "/change-user-status", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('Change User Status')")
    public RedirectView changeUserStatus(@RequestParam(name = "id") Long id, @RequestParam(name = "statusId") Long statusId, RedirectAttributes redirectAttributes) throws RecordNotFoundException {
        User user = userService.getUserById(id);
        user.setUserStatus(userStatusService.getUserStateByid(statusId));
        userService.save(user);
        redirectAttributes.addFlashAttribute("status", this.getStatusSuccess());
        redirectAttributes.addFlashAttribute("message", "Ma'lumot muvvaffaqiyatli tahrirlandi");
        return new RedirectView("/module/user/");
    }

    @RequestMapping(path = "/by-companyId", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('Get by Company')")
    public ModelAndView areaByCompanyId(@RequestParam(name = "companyId") Long companyId, ModelAndView modelAndView) {
        List<User> users = userService.byCompanyId(companyId);
        modelAndView.addObject("users", users);
        modelAndView.setViewName("/pages/modules/users/index");
        return modelAndView;
    }
}
