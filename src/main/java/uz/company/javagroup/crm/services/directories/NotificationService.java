package uz.isd.javagroup.grandcrm.services.directories;


import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface NotificationService {

    List<Notification> findAll();

    List<Object[]> allByUserId(Long userId);

    List<Object[]> getAllByUserIdAndNotViewed(Long userId);

    void saveNotification(Notification notification);

    Notification getNotificationById(Long id) throws RecordNotFoundException;

    void deleteNotification(Long id);

    List<Notification> byNotificationTypeId(Long notificationTypeId);

    List<Notification> byNotificationTemplateId(Long notificationTemplateId);

}
