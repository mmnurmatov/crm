package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    List<User> findByCompanyId(Long companyId);

    List<User> findByRoleId(Long roleId);

    User findByToken(String token);

    @Query(value = "select * from users where company_id = ?1 and role_id = ?2", nativeQuery = true)
    List<User> getUsersByCompanyIdAndRoleId(Long companyId, Long roleId);
}
