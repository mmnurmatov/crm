package uz.isd.javagroup.grandcrm.services.directories;

import uz.isd.javagroup.grandcrm.entity.directories.UserStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface UserStatusService {

    List<UserStatus> findAll();

    void saveUserStatus(UserStatus userStatus);

    UserStatus getUserStateByid(Long id) throws RecordNotFoundException;

    void deleteUserStatus(Long id);
}
