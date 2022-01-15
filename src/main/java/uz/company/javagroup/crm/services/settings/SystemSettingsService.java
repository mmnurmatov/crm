package uz.isd.javagroup.grandcrm.services.settings;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.settings.SystemSetting;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface SystemSettingsService {

    List<SystemSetting> findAll();

    void saveSystemSetting(SystemSetting systemSetting);

    SystemSetting getSystemSettingById(Long id) throws RecordNotFoundException;

    void deleteSystemSetting(Long id);
}
