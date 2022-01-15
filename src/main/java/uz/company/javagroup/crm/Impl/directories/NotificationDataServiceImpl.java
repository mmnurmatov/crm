package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationData;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.NotificationDataRepository;
import uz.isd.javagroup.grandcrm.services.directories.NotificationDataService;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationDataServiceImpl implements NotificationDataService {

    @Autowired
    NotificationDataRepository notificationDataRepository;

    @Override
    public List<NotificationData> findAll() {
        List<NotificationData> notificationData = (List<NotificationData>) notificationDataRepository.findAll();
        return notificationData;
    }

    @Override
    public void saveNotificationData(NotificationData notificationData) {
        notificationDataRepository.save(notificationData);
    }

    @Override
    public NotificationData getNotificationDataById(Long id) throws RecordNotFoundException {
        Optional<NotificationData> notificationDataOpt = notificationDataRepository.findById(id);
        if (notificationDataOpt.isPresent()) return notificationDataOpt.get();
        else throw new RecordNotFoundException("No Notification Data record exists for id: " + id);
    }

    @Override
    public void deleteNotificationData(Long id) {
        notificationDataRepository.deleteById(id);
    }

    @Override
    public List<NotificationData> byNotificationId(Long notificationId) {
        return notificationDataRepository.findByNotificationId(notificationId);
    }
}
