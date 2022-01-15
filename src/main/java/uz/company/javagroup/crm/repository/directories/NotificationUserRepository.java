package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationUser;

import java.util.List;

@Repository
public interface NotificationUserRepository extends CrudRepository<NotificationUser, Long> {

    List<NotificationUser> findByUserId(Long userId);

    List<NotificationUser> findByNotificationId(Long notificationId);

}
