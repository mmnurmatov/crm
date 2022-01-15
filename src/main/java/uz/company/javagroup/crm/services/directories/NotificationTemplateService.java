package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationTemplate;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface NotificationTemplateService {

    List<NotificationTemplate> findAll();

    void saveNotificationTemplate(NotificationTemplate notificationTemplate);

    NotificationTemplate getNotificationTemplateById(Long id) throws RecordNotFoundException;

    void deleteNotificationTemplate(Long id);

}
