package uz.isd.javagroup.grandcrm.services.directories;

import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role saveRole(Role role) throws RecordNotFoundException;

    Role getRoleById(Long id) throws RecordNotFoundException;

    void deleteRole(Long id);

    Role findByCode(String code);
}
