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
@Table(name = "expense_types")
public class ExpenseType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXPENSE_TYPES_SEQUENCE")
    @SequenceGenerator(sequenceName = "EXPENSE_TYPES_SEQUENCE", allocationSize = 1, name = "EXPENSE_TYPES_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "type")
    private Integer type = 1;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;
}
