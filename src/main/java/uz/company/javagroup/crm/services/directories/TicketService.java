package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Ticket;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface TicketService {

    List<Ticket> findAll();

    void saveTicket(Ticket ticket);

    Ticket getTicketById(Long id) throws RecordNotFoundException;

    void deleteTicket(Long id);

    List<Ticket> findAllByUserId(Long id);
}
