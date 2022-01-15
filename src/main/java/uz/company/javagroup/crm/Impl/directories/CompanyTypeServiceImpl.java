package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.CompanyType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.CompanyTypeRepository;
import uz.isd.javagroup.grandcrm.services.directories.CompanyTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyTypeServiceImpl implements CompanyTypeService {

    @Autowired
    CompanyTypeRepository companyTypeRepository;

    @Override
    public List<CompanyType> findAll() {
        List<CompanyType> companyTypes = (List<CompanyType>) companyTypeRepository.findAll();
        return companyTypes;
    }

    @Override
    public void saveCompanyType(CompanyType companyType) {
        companyTypeRepository.save(companyType);
    }

    @Override
    public CompanyType getCompanyTypeById(Long id) throws RecordNotFoundException {
        Optional<CompanyType> companyTypeOpt = companyTypeRepository.findById(id);
        if (companyTypeOpt.isPresent()) return companyTypeOpt.get();
        else throw new RecordNotFoundException("No CompanyType record exists for given id: " + id);
    }

    @Override
    public void deleteCompanyType(Long id) {
        companyTypeRepository.deleteById(id);
    }
}
