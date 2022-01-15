package uz.isd.javagroup.grandcrm.entity.directories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_templates")
public class NotificationTemplate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_TEMPLATES_SEQUENCE")
    @SequenceGenerator(sequenceName = "NOTIFICATION_TEMPLATES_SEQUENCE", allocationSize = 1, name = "NOTIFICATION_TEMPLATES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title_uz")
    private String titleUz;

    @Column(name = "title_ru")
    private String titleRu;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "content_uz")
    private String contentUz;

    @Column(name = "content_ru")
    private String contentRu;

    @Column(name = "content_en")
    private String contentEn;

}
