package uz.isd.javagroup.grandcrm.repository.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.settings.SystemSetting;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSetting, Long> {
}
