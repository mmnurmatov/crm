package uz.isd.javagroup.grandcrm.entity.directories;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.modules.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_files")
public class UserFiles implements Serializable {

    private static final long serialVersionUID = 7783129447407163408L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_FILES_SEQUENCE")
    @SequenceGenerator(sequenceName = "USER_FILES_SEQUENCE", allocationSize = 1, name = "USER_FILES_SEQUENCE")
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "modified_file_name")
    private String modifiedFileName;

    @Column(name = "file_extension")
    private String fileExtension;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

}
