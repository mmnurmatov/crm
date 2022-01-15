package uz.isd.javagroup.grandcrm.repository.directories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.directories.Region;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByAreaId(Long areaId);
}
