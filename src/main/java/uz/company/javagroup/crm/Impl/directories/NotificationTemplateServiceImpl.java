package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.Notification;
import uz.isd.javagroup.grandcrm.entity.directories.NotificationTemplate;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.NotificationTemplateRepository;
import uz.isd.javagroup.grandcrm.services.directories.NotificationTemplateService;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    @Autowired
    NotificationTemplateRepository notificationTemplateRepository;


    @Override
    public List<NotificationTemplate> findAll() {
        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findAll();
        return notificationTemplates;
    }

    @Override
    public void saveNotificationTemplate(NotificationTemplate notificationTemplate) {
        notificationTemplateRepository.save(notificationTemplate);
    }

    @Override
    public NotificationTemplate getNotificationTemplateById(Long id) throws RecordNotFoundException {
        Optional<NotificationTemplate> notificationTemplateOpt = notificationTemplateRepository.findById(id);
        if (notificationTemplateOpt.isPresent()) return notificationTemplateOpt.get();
        else throw new RecordNotFoundException("No Notification template record exists for id: " + id);
    }

    @Override
    public void deleteNotificationTemplate(Long id) {
        notificationTemplateRepository.deleteById(id);
    }

}
