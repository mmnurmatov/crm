package uz.isd.javagroup.grandcrm.services.directories;

import uz.isd.javagroup.grandcrm.entity.directories.CompanyStatus;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface CompanyStatusService {

    List<CompanyStatus> findAll();

    void saveCompanyStatus(CompanyStatus companyStatus);

    CompanyStatus getCompanyStatusById(Long id) throws RecordNotFoundException;

    void deleteCompanyStatus(Long id);
}
