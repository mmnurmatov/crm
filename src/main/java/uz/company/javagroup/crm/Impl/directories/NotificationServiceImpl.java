package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.NotificationRepository;
import uz.isd.javagroup.grandcrm.services.directories.NotificationService;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public List<Notification> findAll() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications;
    }

    @Override
    public List<Object[]> allByUserId(Long userId) {
        return notificationRepository.allByUserId(userId);
    }

    @Override
    public List<Object[]> getAllByUserIdAndNotViewed(Long userId) {
        return notificationRepository.getAllByUserIdAndNotViewed(userId);
    }

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(Long id) throws RecordNotFoundException {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isPresent()) return notificationOpt.get();
        else throw new RecordNotFoundException("No Notification record exists for id: " + id);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> byNotificationTypeId(Long notificationTypeId) {
        return notificationRepository.findByNotificationTypeId(notificationTypeId);
    }

    @Override
    public List<Notification> byNotificationTemplateId(Long notificationTemplateId) {
        return notificationRepository.findByNotificationTemplateId(notificationTemplateId);
    }


}
