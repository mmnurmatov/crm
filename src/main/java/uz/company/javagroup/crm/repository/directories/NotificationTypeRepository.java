package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationType;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
}
