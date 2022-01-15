package uz.isd.javagroup.grandcrm.entity.directories;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATIONS_SEQUENCE")
    @SequenceGenerator(sequenceName = "NOTIFICATIONS_SEQUENCE", allocationSize = 1, name = "NOTIFICATIONS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_type_id")
    @JsonIgnore
    NotificationType notificationType;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_template_id")
    @JsonIgnore
    NotificationTemplate notificationTemplate;

    @Column(name = "created_at")
    private  Date createdAt;


}
