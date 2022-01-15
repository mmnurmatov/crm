package uz.isd.javagroup.grandcrm.Impl.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.settings.SystemSetting;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.settings.SystemSettingsRepository;
import uz.isd.javagroup.grandcrm.services.settings.SystemSettingsService;

import java.util.List;
import java.util.Optional;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    @Autowired
    SystemSettingsRepository systemSettingsRepository;


    @Override
    public List<SystemSetting> findAll() {
        List<SystemSetting> systemSettings = systemSettingsRepository.findAll();
        return systemSettings;
    }

    @Override
    public void saveSystemSetting(SystemSetting systemSetting) {
        systemSettingsRepository.save(systemSetting);
    }

    @Override
    public SystemSetting getSystemSettingById(Long id) throws RecordNotFoundException {
        Optional<SystemSetting> systemSettingOpt = systemSettingsRepository.findById(id);
        if (systemSettingOpt.isPresent()) return systemSettingOpt.get();
        else throw new RecordNotFoundException("No System setting record exists for id: " + id);
    }

    @Override
    public void deleteSystemSetting(Long id) {
        systemSettingsRepository.deleteById(id);
    }
}
