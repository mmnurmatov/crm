package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.MessageType;

@Repository
public interface MessageTypeRepository extends CrudRepository<MessageType, Long> {
}
