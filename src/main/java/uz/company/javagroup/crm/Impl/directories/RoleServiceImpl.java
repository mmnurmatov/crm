package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Role;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.RoleRepository;
import uz.isd.javagroup.grandcrm.services.directories.RoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    @Override
    public Role saveRole(Role role) {

        Role roleDB = roleRepository.save(role);
        return roleDB;
    }

    @Override
    public Role getRoleById(Long id) throws RecordNotFoundException {
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (roleOpt.isPresent()) return roleOpt.get();
        else throw new RecordNotFoundException("No Role record exists for id: " + id);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role findByCode(String code) {
        return roleRepository.findByCode(code);
    }
}
