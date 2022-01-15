package uz.isd.javagroup.grandcrm.entity.directories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.modules.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification_users")
public class NotificationUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_USERS_SEQUENCE")
    @SequenceGenerator(sequenceName = "NOTIFICATION_USERS_SEQUENCE", allocationSize = 1, name = "NOTIFICATION_USERS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id")
    @JsonIgnore
    Notification notification;

    @Column(name = "is_view")
    private Boolean isView;

    @Column(name = "view_date")
    private Date viewDate;



}
