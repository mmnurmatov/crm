package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.entity.directories.UserStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 5350402877685656351L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_status_id")
    @JsonIgnore
    UserStatus userStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonIgnore
    Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    Company company;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQUENCE")
    @SequenceGenerator(sequenceName = "USERS_SEQUENCE", allocationSize = 1, name = "USERS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password is required.")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false, length = 120, unique = true, name = "email")
    @NotEmpty(message = "Email is required.")
    @Email
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "last_login_at")
    private Date lastLoginAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "is_email_valid")
    private Boolean isEmailValid = false;

    @Column(name = "token")
    private String token;

    @Column(name = "image_url")
    private String ImageUrl;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column
    private Boolean enabled = true;

    @Transient
    private MultipartFile files;

    @Transient
    private String removeImages;

    public User(User user) {

        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.enabled = user.isEnabled();
        this.role = user.getRole();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
        getRole().getPermissions().forEach(permission -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
        });

        return grantedAuthorities;
    }

    public String getFullName() {
        return firstName + " " + secondName + " " + lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
