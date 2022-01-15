package uz.isd.javagroup.grandcrm.repository.directories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationData;

import java.util.List;

@Repository
public interface NotificationDataRepository extends CrudRepository<NotificationData, Long> {

    List<NotificationData> findByNotificationId(Long notificationId);

}
