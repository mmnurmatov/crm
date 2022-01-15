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
@Table(name = "notification_data")
public class NotificationData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_DATA_SEQUENCE")
    @SequenceGenerator(sequenceName = "NOTIFICATION_DATA_SEQUENCE", allocationSize = 1, name = "NOTIFICATION_DATA_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id")
    @JsonIgnore
    Notification notification;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

}
