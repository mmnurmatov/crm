package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationData;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface NotificationDataService {

    List<NotificationData> findAll();

    void saveNotificationData(NotificationData notificationData);

    NotificationData getNotificationDataById(Long id) throws RecordNotFoundException;

    void deleteNotificationData(Long id);

    List<NotificationData> byNotificationId(Long notificationId);

}
