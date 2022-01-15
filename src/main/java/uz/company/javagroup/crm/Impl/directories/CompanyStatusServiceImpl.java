package uz.isd.javagroup.grandcrm.Impl.directories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.directories.CompanyStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.directories.CompanyStatusRepository;
import uz.isd.javagroup.grandcrm.services.directories.CompanyStatusService;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyStatusServiceImpl implements CompanyStatusService {

    @Autowired
    CompanyStatusRepository companyStatusRepository;

    @Override
    public List<CompanyStatus> findAll() {
        List<CompanyStatus> companyStatuses = (List<CompanyStatus>) companyStatusRepository.findAll();
        return companyStatuses;
    }

    @Override
    public void saveCompanyStatus(CompanyStatus companyStatus) {
        companyStatusRepository.save(companyStatus);
    }

    @Override
    public CompanyStatus getCompanyStatusById(Long id) throws RecordNotFoundException {
        Optional<CompanyStatus> companyStatusOpt = companyStatusRepository.findById(id);
        if (companyStatusOpt.isPresent()) return companyStatusOpt.get();
        else throw new RecordNotFoundException("No CompanyStatus record exists for given id: " + id);
    }

    @Override
    public void deleteCompanyStatus(Long id) {
        companyStatusRepository.deleteById(id);
    }
}
