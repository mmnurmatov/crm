package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface AreaService {
    List<Area> findAll();

    void saveArea(Area area);

    Area getAreaById(Long id) throws RecordNotFoundException;

    void deleteArea(Long id);

    List<Area> byCountryId(Long countryId);
}
