package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Region;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface RegionService {
    List<Region> findAll();

    void saveRegion(Region region);

    Region getRegionById(Long id) throws RecordNotFoundException;

    void deleteRegion(Long id);

    List<Region> byAreaId(Long areaId);
}
