package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    List<Notification> findByNotificationTypeId(Long notificationTypeId);

    List<Notification> findByNotificationTemplateId(Long notificationTemplateId);

//    @Query(value = "select  IFICATION_TYPES T on n.NOTIFICATION_TYPE_ID = T.ID where NU.USER_ID=?1", nativeQuery = true )
//    List<Notification> getAllByUserId(Long userId);

//    @Query(value = "select  n.ID, T.NAME as notification_type, NT.TITLE_UZ as NOTIFICATION_TITLE,\n" +
//            "       REPLACE(NT.CONTENT_UZ, concat(concat('{', ND.KEY), '}'), ND.VALUE) AS MESSAGE,\n" +
//            "       n.CREATED_AT from NOTIFICATIONS n left join NOTIFICATION_DATA ND on n.ID = ND.NOTIFICATION_ID left join NOTIFICATION_USERS NU on n.ID = NU.NOTIFICATION_ID left join NOTIFICATION_TEMPLATES NT on n.NOTIFICATION_TEMPLATE_ID = NT.ID left join NOTIFICATION_TYPES T on n.NOTIFICATION_TYPE_ID = T.ID where NU.USER_ID=?1", nativeQuery = true )
////    @Query(value = "select id, NOTIFICATION_TYPE_ID  from NOTIFICATIONS n", nativeQuery = true )
//    List<Object[]> getAllByUserId(Long userId);

    @Query("select n, nd, nu, nt, t from Notification n " +
            "left join NotificationData nd on n.id = nd.notification.id " +
            "left join NotificationUser nu on n.id = nu.notification.id " +
            "left join NotificationTemplate nt on n.notificationTemplate.id = nt.id " +
            "left join NotificationType t on n.notificationType.id = t.id " +
            "where nu.user.id=?1")
//    @Query(value = "select id, NOTIFICATION_TYPE_ID  from NOTIFICATIONS n", nativeQuery = true )
    List<Object[]> allByUserId(Long userId);

    @Query("select n, nd, nu, nt, t from Notification n " +
            "left join NotificationData nd on n.id = nd.notification.id " +
            "left join NotificationUser nu on n.id = nu.notification.id " +
            "left join NotificationTemplate nt on n.notificationTemplate.id = nt.id " +
            "left join NotificationType t on n.notificationType.id = t.id " +
            "where nu.user.id=?1 and nu.isView=false")
//    @Query(value = "select id, NOTIFICATION_TYPE_ID  from NOTIFICATIONS n", nativeQuery = true )
    List<Object[]> getAllByUserIdAndNotViewed(Long userId);

}
