package uz.isd.javagroup.grandcrm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import uz.isd.javagroup.grandcrm.Impl.directories.RoleServiceImpl;
import uz.isd.javagroup.grandcrm.Impl.modules.UserServiceImpl;

import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.services.directories.PermissionService;
import uz.isd.javagroup.grandcrm.utility.RbacAnnotationScan;
import uz.isd.javagroup.grandcrm.utility.SecurityUtility;

import java.util.Date;

@Slf4j
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class GrandcrmApplication implements CommandLineRunner {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final PermissionService permissionService;


    @Autowired
    public GrandcrmApplication(UserServiceImpl userService,
                RoleServiceImpl roleService,
                PermissionService permissionService) {

        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GrandcrmApplication.class, args);
    }

    @Override
    public void run(String... args) {

        new RbacAnnotationScan().start(permissionService);
        new RbacAnnotationScan().startView(permissionService);
//
//        Role role = new Role();
//        role.setNameRu("Админ");
//        role.setNameUz("Admin");
//        role.setCode("ADMIN");
//        role.setDescription("Initial generated");
//        try {
//            roleService.saveRole(role);
//        } catch (Exception e) {
//            log.info("Such Role record exists");
//        }
//
//        User user1 = new User();
//        user1.setUsername("superadmin");
//        user1.setPassword(SecurityUtility.passwordEncoder().encode("superadmin"));
//        user1.setEmail("admin1@gmail.com");
//        user1.setFirstName("Humoyunmirzo");
//        user1.setSecondName("Sodiqov");
//        user1.setLastName("Nurillo o'g'li");
//        user1.setCreatedAt(new Date());
//        user1.setPhoneNumber("+99899-821-94-07");
//        user1.setRole(role);
////
//        User user2 = new User();
//        user2.setUsername("admin");
//        user2.setPassword(SecurityUtility.passwordEncoder().encode("admin"));
//        user2.setEmail("admin@gmail.com");
//        user2.setFirstName("Muzaffar");
//        user2.setSecondName("Murodov");
//        user2.setLastName("Murodovich");
//        user2.setCreatedAt(new Date());
//        user2.setPhoneNumber("+99893-594-90-30");
//        user2.setRole(role);
//        userService.createUser(user2);
//
//        User user3 = new User();
//        user3.setUsername("user");
//        user3.setPassword(SecurityUtility.passwordEncoder().encode("user"));
//        user3.setEmail("user@gmail.com");
//        user3.setFirstName("Maqsudbek");
//        user3.setSecondName("Nurmatov");
//        user1.setLastName("Ma'ruf o'g'li");
//        user3.setCreatedAt(new Date());
//        user3.setPhoneNumber("+99891-348-42-42");
//        user3.setRole(role);
//
//        User user4 = new User();
//        user4.setUsername("operator");
//        user4.setPassword(SecurityUtility.passwordEncoder().encode("operator"));
//        user4.setEmail("user1@gmail.com");
//        user4.setFirstName("Bozorov");
//        user4.setSecondName("Ma'mur");
//        user1.setLastName("Bozorovich");
//        user4.setCreatedAt(new Date());
//        user4.setPhoneNumber("+99890-553-37-15");
//        user4.setRole(role);
//
//        userService.createUser(user1);
//        userService.createUser(user2);
//        userService.createUser(user3);
//        userService.createUser(user4);
    }
}
