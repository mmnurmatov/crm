package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.ExpenseType;

import java.util.List;

@Repository
public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long> {
    List<ExpenseType> findAllByType(Integer type);
}
