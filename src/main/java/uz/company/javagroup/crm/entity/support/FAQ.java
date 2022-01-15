package uz.isd.javagroup.grandcrm.entity.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faq")
public class FAQ implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAQ_SEQUENCE")
    @SequenceGenerator(sequenceName = "FAQ_SEQUENCE", allocationSize = 1, name = "FAQ_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "question_ru")
    private String questionRu;

    @Column(name = "question_uz")
    private String questionUz;

    @Column(name = "question_en")
    private String questionEn;

    @Column(name = "answer_ru")
    private String answerRu;

    @Column(name = "answer_uz")
    private String answerUz;

    @Column(name = "answer_en")
    private String answerEn;

    @Column(name = "ordering")
    private int ordering;


}
