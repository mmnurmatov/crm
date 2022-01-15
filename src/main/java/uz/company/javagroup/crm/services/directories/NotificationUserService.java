package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationUser;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface NotificationUserService {

    List<NotificationUser> findAll();

    void saveNotificationUser(NotificationUser notificationUser);

    NotificationUser getNotificationUserById(Long id) throws RecordNotFoundException;

    void deleteNotificationUser(Long id);

    List<NotificationUser> byUserId(Long userId);

    List<NotificationUser> byNotificationId(Long notificationId);

}
