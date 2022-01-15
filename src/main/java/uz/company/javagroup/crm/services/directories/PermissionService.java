package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Permission;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface PermissionService {

    List<Permission> findAll(Sort sort);

    List<Permission> findAllByActionName();

    List<Object[]> findAllByMenuNameAndParentIdIsNull(Long roleId);

    void savePermission(Permission permission);

    Permission getPermissionById(Long id) throws RecordNotFoundException;

    void deletePermission(Long id);

    Permission getPermissionByName(String name);

    Permission getPermissionByNameAndType(String name, String type);

    List<Object[]> findAllByParentId(Long roleId, Long permId);

    void deleteOldMenuConnections(Long roleId);

    void deleteOldConnections(Long roleId);
}
