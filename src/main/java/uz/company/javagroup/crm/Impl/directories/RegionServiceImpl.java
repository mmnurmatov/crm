package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Region;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.RegionRepository;
import uz.isd.javagroup.grandcrm.services.directories.RegionService;

import java.util.List;
import java.util.Optional;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionRepository regionRepository;

    @Override
    public List<Region> findAll() {
        List<Region> regions = regionRepository.findAll();
        return regions;
    }

    @Override
    public void saveRegion(Region region) {
        regionRepository.save(region);
    }

    @Override
    public Region getRegionById(Long id) throws RecordNotFoundException {
        Optional<Region> regionOpt = regionRepository.findById(id);
        if (regionOpt.isPresent()) return regionOpt.get();
        else throw new RecordNotFoundException("No Region record exists for id: " + id);
    }

    @Override
    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    @Override
    public List<Region> byAreaId(Long areaId) {
        return regionRepository.findByAreaId(areaId);
    }
}
