package uz.isd.javagroup.grandcrm.repository.modules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.modules.Contragent;

@Repository
public interface ContragentRepository extends JpaRepository<Contragent, Long> {
}
