package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface NotificationTypeService {

    List<NotificationType> findAll();

    void saveNotificationType(NotificationType notificationType);

    NotificationType getNotificationTypeById(Long id) throws RecordNotFoundException;

    void deleteNotificationType(Long id);

}
