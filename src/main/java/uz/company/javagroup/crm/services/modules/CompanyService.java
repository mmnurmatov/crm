package uz.isd.javagroup.grandcrm.services.modules;

import uz.isd.javagroup.grandcrm.entity.modules.Company;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface CompanyService {

    List<Company> findAll();

    void saveCompany(Company company);

    Company getCompanyById(Long id) throws RecordNotFoundException;

    void deleteCompany(Long id);
}
