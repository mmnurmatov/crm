package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.directories.MessageType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGES_SEQUENCE")
    @SequenceGenerator(sequenceName = "MESSAGES_SEQUENCE", allocationSize = 1, name = "MESSAGES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "message_type_id")
    @JsonIgnore
    MessageType messageType;

    @Column(name = "code")
    private String code;

    @Column(name = "title_uz")
    private String titleUz;

    @Column(name = "title_ru")
    private String titleRu;

    @Column(name = "title_en")
    private String titleEn;

    @Column(name = "text_uz")
    private String textUz;

    @Column(name = "text_ru")
    private String textRu;

    @Column(name = "text_en")
    private String textEn;
}
