package uz.isd.javagroup.grandcrm.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationUser;
import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.repository.directories.NotificationUserRepository;
import uz.isd.javagroup.grandcrm.repository.modules.UserRepository;

import java.util.List;

@Component
public class NotificationSending {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationUserRepository notificationUserRepository;

    private static User user;
    private static Company company;
    private static Role role;
    private static Notification notification;


    public  NotificationSending(){

    }

    public NotificationSending(Notification notification, User user){

        this.user = user;
        this.notification = notification;

    }

    public NotificationSending(Notification notification, Company company){

        this.company =  company;
        this.notification = notification;

    }

    public NotificationSending(Notification notification, Role role){

        this.role =  role;
        this.notification = notification;

    }

    public NotificationUser sendingNotification() {
        if(user != null){
            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setUser(this.user);
            notificationUser.setNotification(this.notification);
            notificationUser.setIsView(false);
            return notificationUser;
        } else if (company != null){

            List<User> companyUsers = userRepository.findByCompanyId(company.getId());
            for (User companyUser: companyUsers) {

                NotificationUser notificationUser = new NotificationUser();
                notificationUser.setUser(companyUser);
                notificationUser.setNotification(this.notification);
                notificationUser.setIsView(false);
                return notificationUser;
            }
        } else if (role != null){

            List<User> roleUsers = userRepository.findByRoleId(role.getId());
            for (User roleUser: roleUsers) {

                NotificationUser notificationUser = new NotificationUser();
                notificationUser.setUser(roleUser);
                notificationUser.setNotification(this.notification);
                notificationUser.setIsView(false);
                return notificationUser;
            }
        }

        return null;
    }

}
