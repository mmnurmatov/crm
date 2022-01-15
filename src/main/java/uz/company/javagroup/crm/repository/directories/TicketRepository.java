package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>, QueryByExampleExecutor<Ticket> {
    List<Ticket> findAllByUserIdOrderByIdDesc(Long id);
}
