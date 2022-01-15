package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.CompanyRepository;
import uz.isd.javagroup.grandcrm.services.modules.CompanyService;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<Company> findAll() {
        List<Company> companies = (List<Company>) companyRepository.findAll();
        return companies;
    }

    @Override
    public void saveCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) throws RecordNotFoundException {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) return companyOpt.get();
        else throw new RecordNotFoundException("No Company record exists for id: " + id);
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
