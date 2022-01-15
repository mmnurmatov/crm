package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Permission;
import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.PermissionRepository;
import uz.isd.javagroup.grandcrm.repository.directories.RoleRepository;
import uz.isd.javagroup.grandcrm.services.directories.PermissionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Permission> findAll(Sort sort) {
        List<Permission> permissions = permissionRepository.findAll(sort);
        return permissions;
    }

    @Override
    public List<Permission> findAllByActionName() {
        return permissionRepository.findAllByActionName();
    }

    @Override
    public List<Object[]> findAllByMenuNameAndParentIdIsNull(Long roleId) {
        return permissionRepository.findAllByMenuNameAndParentIdIsNull(roleId, "Menu");
    }

    @Override
    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }

    @Override
    public Permission getPermissionById(Long id) throws RecordNotFoundException {
        Optional<Permission> permissionOpt = permissionRepository.findById(id);
        if (permissionOpt.isPresent()) return permissionOpt.get();
        else throw new RecordNotFoundException("No Permission record exists for id: " + id);
    }

    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission getPermissionByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public Permission getPermissionByNameAndType(String name, String type) {
        return permissionRepository.findByNameAndType(name, type);
    }

    @Override
    public List<Object[]> findAllByParentId(Long roleId, Long permId) {
        return permissionRepository.findAllByParentId(roleId, permId);
    }

    @Override
    public void deleteOldMenuConnections(Long roleId) {

        List<Long> permIds = permissionRepository.getPermIds(roleId);
        Optional<Role> role = roleRepository.findById(roleId);
        role.get().setPermissions(new ArrayList<>());

        if (!role.isPresent())  return;
        permIds.forEach(permId -> {
            Optional<Permission> permission = permissionRepository.findById(permId);
            permission.ifPresent(value -> role.get().getPermissions().add(value));
        });

        roleRepository.save(role.get());
    }

    @Override
    public void deleteOldConnections(Long roleId) {

        List<Long> permIds = permissionRepository.getMenuIds(roleId);

        Optional<Role> role = roleRepository.findById(roleId);

        role.get().setPermissions(new ArrayList<>());

        if (!role.isPresent())  return;
        permIds.forEach(permId -> {
            Optional<Permission> permission = permissionRepository.findById(permId);
            permission.ifPresent(value -> role.get().getPermissions().add(value));
        });

        roleRepository.save(role.get());

    }
}
