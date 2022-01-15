package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.Permission;
import uz.isd.javagroup.grandcrm.entity.directories.Role;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByCode(String code);

    @Query("SELECT p FROM Permission p WHERE p.id IN (SELECT p.id FROM Role r LEFT JOIN r.permissions p WHERE r.id = ?1) ORDER BY p.name DESC")
    List<Permission> byRoleId(Long roleId);

    @Query(nativeQuery = true, value = "select count(*) from PERMISSION_ROLE where ROLE_ID=?1 and PERMISSION_ID=?2")
    int checkPermissionIsSelected(Long roleId, Long permissionId);

}
