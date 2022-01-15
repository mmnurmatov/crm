package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.Country;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.CountryRepository;
import uz.isd.javagroup.grandcrm.services.directories.CountryService;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        List<Country> countries = countryRepository.findAll();
        return countries;
    }

    @Override
    public void saveCountry(Country country) {
        countryRepository.save(country);
    }

    @Override
    public Country getCountryById(Long id) throws RecordNotFoundException {
        Optional<Country> countryOpt = countryRepository.findById(id);
        if (countryOpt.isPresent()) return countryOpt.get();
        else throw new RecordNotFoundException("No Country record exists for id: " + id);
    }

    @Override
    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }
}
