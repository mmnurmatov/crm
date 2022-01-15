package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.TicketFile;

import java.util.List;

@Repository
public interface TicketFileRepository extends CrudRepository<TicketFile, Long> {
    List<TicketFile> findAllByTicketMessageId(Long ticketMessageId);
}
