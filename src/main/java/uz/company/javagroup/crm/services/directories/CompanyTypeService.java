package uz.isd.javagroup.grandcrm.services.directories;

import uz.isd.javagroup.grandcrm.entity.directories.CompanyType;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface CompanyTypeService {

    List<CompanyType> findAll();

    void saveCompanyType(CompanyType companyType);

    CompanyType getCompanyTypeById(Long id) throws RecordNotFoundException;

    void deleteCompanyType(Long id);
}
