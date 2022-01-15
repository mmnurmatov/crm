package uz.isd.javagroup.grandcrm.entity.directories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_statuses")
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 5350402877685656351L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_STATUSES_SEQUENCE")
    @SequenceGenerator(sequenceName = "USER_STATUSES_SEQUENCE", allocationSize = 1, name = "USER_STATUSES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "description")
    private String description;
}
