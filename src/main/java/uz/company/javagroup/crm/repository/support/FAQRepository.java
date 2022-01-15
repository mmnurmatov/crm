package uz.isd.javagroup.grandcrm.repository.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.support.FAQ;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
