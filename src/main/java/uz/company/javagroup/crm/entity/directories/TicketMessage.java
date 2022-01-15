package uz.isd.javagroup.grandcrm.entity.directories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.modules.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket_messages")
public class TicketMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TICKET_MESSAGES_SEQUENCE")
    @SequenceGenerator(sequenceName = "TICKET_MESSAGES_SEQUENCE", allocationSize = 1, name = "TICKET_MESSAGES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    @JsonIgnore
    private Ticket ticket;

    @Column(name = "adminId")
    private Long adminId;

    @Column(name = "message")
    private String message;

    @Column(name = "message_date")
    private Date date;

    @Transient
    List<TicketFile> ticketFiles;
    @Transient
    User admin;
}
