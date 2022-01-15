package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationUser;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.NotificationUserRepository;
import uz.isd.javagroup.grandcrm.services.directories.NotificationUserService;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationUserServiceImpl implements NotificationUserService {

    @Autowired
    NotificationUserRepository notificationUserRepository;

    @Override
    public List<NotificationUser> findAll() {
        List<NotificationUser> notificationUsers = (List<NotificationUser>) notificationUserRepository.findAll();
        return notificationUsers;
    }

    @Override
    public void saveNotificationUser(NotificationUser notificationUser) {
        notificationUserRepository.save(notificationUser);
    }

    @Override
    public NotificationUser getNotificationUserById(Long id) throws RecordNotFoundException {
        Optional<NotificationUser> notificationUserOpt = notificationUserRepository.findById(id);
        if (notificationUserOpt.isPresent()) return notificationUserOpt.get();
        else throw new RecordNotFoundException("No Notification User record exists for id: " + id);
    }

    @Override
    public void deleteNotificationUser(Long id) {
        notificationUserRepository.deleteById(id);
    }

    @Override
    public List<NotificationUser> byUserId(Long userId) {
        return notificationUserRepository.findByUserId(userId);
    }

    @Override
    public List<NotificationUser> byNotificationId(Long notificationId) {
        return notificationUserRepository.findByNotificationId(notificationId);
    }

}
