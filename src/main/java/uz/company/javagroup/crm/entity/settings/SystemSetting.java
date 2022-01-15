package uz.isd.javagroup.grandcrm.entity.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_setting_properties")
public class SystemSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYSTEM_SETTING_PROPERTIES_SEQUENCE")
    @SequenceGenerator(sequenceName = "SYSTEM_SETTING_PROPERTIES_SEQUENCE", allocationSize = 1, name = "SYSTEM_SETTING_PROPERTIES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "system_key")
    private String key;

    @Column(name = "system_value")
    private String value;

}
