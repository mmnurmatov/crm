package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.Permission;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, PagingAndSortingRepository<Permission, Long> {
    Permission findByName(String name);

    Permission findByNameAndType(String name, String type);

    @Query("SELECT p, (SELECT count(r) FROM Role r LEFT JOIN r.permissions per WHERE r.id =?1 AND per.id = p.id) FROM Permission p WHERE p.parentId IS NULL AND p.type LIKE ?2% ORDER BY p.type, p.id")
    List<Object[]> findAllByMenuNameAndParentIdIsNull(Long roleId, String menu);

    @Query(nativeQuery = true, value = "select * from PERMISSIONS where TYPE NOT like 'Menu%' order by NAME desc")
    List<Permission> findAllByActionName();

    @Query("SELECT p, (SELECT count(r) FROM Role r LEFT JOIN r.permissions per WHERE r.id =?1 AND per.id = p.id) FROM Permission p WHERE p.parentId = ?2 ORDER BY p.type, p.id")
    List<Object[]> findAllByParentId(Long roleId, Long permId);

    @Modifying
    @Query(value = "select p.ID from PERMISSION_ROLE pr left join PERMISSIONS P on pr.PERMISSION_ID = P.ID left join ROLES R on R.ID = pr.ROLE_ID where pr.ROLE_ID = ?1 and TYPE not like 'Menu%'", nativeQuery = true)
    List<Long> getPermIds(Long roleId);

    @Modifying
    @Query(value = "select p.ID from PERMISSION_ROLE pr left join PERMISSIONS P on pr.PERMISSION_ID = P.ID left join ROLES R on R.ID = pr.ROLE_ID where pr.ROLE_ID = ?1 and TYPE like 'Menu%'", nativeQuery = true)
    List<Long> getMenuIds(Long roleId);

}
