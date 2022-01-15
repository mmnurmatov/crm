package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.NotificationTypeRepository;
import uz.isd.javagroup.grandcrm.services.directories.NotificationTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationTypeServiceImpl implements NotificationTypeService {

    @Autowired
    NotificationTypeRepository notificationTypeRepository;

    @Override
    public List<NotificationType> findAll() {
        List<NotificationType> notificationTypes = notificationTypeRepository.findAll();
        return notificationTypes;
    }

    @Override
    public void saveNotificationType(NotificationType notificationType) {
        notificationTypeRepository.save(notificationType);
    }

    @Override
    public NotificationType getNotificationTypeById(Long id) throws RecordNotFoundException {
        Optional<NotificationType> notificationTypeOpt = notificationTypeRepository.findById(id);
        if (notificationTypeOpt.isPresent()) return notificationTypeOpt.get();
        else throw new RecordNotFoundException("No Notification type record exists for id: " + id);
    }

    @Override
    public void deleteNotificationType(Long id) {
        notificationTypeRepository.deleteById(id);
    }
}
