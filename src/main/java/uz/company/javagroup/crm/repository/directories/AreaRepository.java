package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.Country;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {

    List<Area> findByCountryId(Long countryId);
}
