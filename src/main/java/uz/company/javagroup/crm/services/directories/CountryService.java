package uz.isd.javagroup.grandcrm.services.directories;

import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

@Service
public interface CountryService {
    List<Country> findAll();

    void saveCountry(Country country);

    Country getCountryById(Long id) throws RecordNotFoundException;

    void deleteCountry(Long id);
}
