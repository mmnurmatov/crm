package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.UserFiles;

import java.util.List;

@Repository
public interface UserFileRepository extends CrudRepository<UserFiles, Long> {

    UserFiles findByUserId(Long userId);

    List<UserFiles> findAllByUserId(Long id);
}
