package uz.isd.javagroup.grandcrm.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.modules.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VERIFICATION_TOKEN_SEQUENCE")
    @SequenceGenerator(sequenceName = "VERIFICATION_TOKEN_SEQUENCE", allocationSize = 1, name = "VERIFICATION_TOKEN_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;


}
