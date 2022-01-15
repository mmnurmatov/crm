package uz.isd.javagroup.grandcrm.entity.directories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket_files")
public class TicketFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TICKET_FILES_SEQUENCE")
    @SequenceGenerator(sequenceName = "TICKET_FILES_SEQUENCE", allocationSize = 1, name = "TICKET_FILES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_message_id")
    @JsonIgnore
    private TicketMessage ticketMessage;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "modified_file_name")
    private String modifiedFileName;

    @Column(name = "file_extension")
    private String fileExtension;

    @Transient
    private MultipartFile file;
}
