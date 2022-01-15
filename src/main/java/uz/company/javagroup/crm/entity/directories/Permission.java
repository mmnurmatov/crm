package uz.isd.javagroup.grandcrm.entity.directories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class Permission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSIONS_SEQUENCE")
    @SequenceGenerator(sequenceName = "PERMISSIONS_SEQUENCE", allocationSize = 1, name = "PERMISSIONS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "is_module")
    private Boolean isModule;

    @Transient
    private int isSelected;

    @Transient
    List<Object[]> children;
}
