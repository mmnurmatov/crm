package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}
