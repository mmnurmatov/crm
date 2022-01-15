package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.UserStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.UserStatusRepository;
import uz.isd.javagroup.grandcrm.services.directories.UserStatusService;

import java.util.List;
import java.util.Optional;

@Service
public class UserStatusServiceImpl implements UserStatusService {

    @Autowired
    UserStatusRepository userStatusRepository;

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> userStatuses = (List<UserStatus>) userStatusRepository.findAll();
        return userStatuses;
    }

    @Override
    public void saveUserStatus(UserStatus userStatus) {
        userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus getUserStateByid (Long id) throws RecordNotFoundException {

        Optional<UserStatus> userState = userStatusRepository.findById(id);

        if(userState.isPresent()){

            return userState.get();
        }else {

            throw new RecordNotFoundException("No userState record exist for given id");
        }

    }

    @Override
    public void deleteUserStatus(Long id) {
        userStatusRepository.deleteById(id);
    }
}
