package uz.isd.javagroup.grandcrm.services.modules;

import org.springframework.web.multipart.MultipartFile;
import uz.isd.javagroup.grandcrm.entity.directories.UserFiles;
import uz.isd.javagroup.grandcrm.entity.modules.User;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> findAll();

    User createUser(User user) throws Exception;

    UserFiles findFileByUserId(Long userId);

    void save(User user);

    User getUserById(Long id) throws RecordNotFoundException;

    void updateUser(User user, MultipartFile files) throws IOException;

    User getUserByEmail(String email);

    User getUserByUsername(String username);

    User getUserByPhoneNumber(String phone);

    List<User> byCompanyId(Long companyId);

    List<User> byRoleId(Long roleId);

    User getByToken(String token);

    List<User> getUsersByCompanyIdAndRoleId(Long companyId, Long roleId);
}
