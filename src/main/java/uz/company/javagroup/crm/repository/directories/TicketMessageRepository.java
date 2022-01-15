package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.TicketMessage;

import java.util.List;

@Repository
public interface TicketMessageRepository extends CrudRepository<TicketMessage, Long> {
    List<TicketMessage> findAllByTicketIdOrderByIdDesc(Long ticketId);
}
