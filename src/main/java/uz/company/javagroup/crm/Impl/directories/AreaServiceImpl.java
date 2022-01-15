package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Area;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.AreaRepository;
import uz.isd.javagroup.grandcrm.services.directories.AreaService;

import java.util.List;
import java.util.Optional;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaRepository areaRepository;

    @Override
    public List<Area> findAll() {
        List<Area> areas = areaRepository.findAll();
        return areas;
    }

    @Override
    public void saveArea(Area area) {
        areaRepository.save(area);
    }

    @Override
    public Area getAreaById(Long id) throws RecordNotFoundException {
        Optional<Area> areaOpt = areaRepository.findById(id);
        if (areaOpt.isPresent()) return areaOpt.get();
        else throw new RecordNotFoundException("No Area record exists for id: " + id);
    }

    @Override
    public void deleteArea(Long id) {
        areaRepository.deleteById(id);
    }

    @Override
    public List<Area> byCountryId(Long countryId) {
        return areaRepository.findByCountryId(countryId);
    }
}
