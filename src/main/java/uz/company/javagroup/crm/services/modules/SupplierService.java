package uz.isd.javagroup.grandcrm.services.modules;

import uz.isd.javagroup.grandcrm.entity.modules.Supplier;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface SupplierService {

    List<Supplier> findAll();

    void saveSupplier(Supplier supplier);

    Supplier getSupplierById(Long id) throws RecordNotFoundException;

    void deleteSupplier(Long id);

}
