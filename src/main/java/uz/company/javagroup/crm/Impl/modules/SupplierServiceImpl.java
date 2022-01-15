package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.modules.Supplier;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.SupplierRepository;
import uz.isd.javagroup.grandcrm.services.modules.SupplierService;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    SupplierRepository supplierRepository;

    @Override
    public List<Supplier> findAll() {
        List<Supplier> supplierList = (List<Supplier>) supplierRepository.findAll();
        return supplierList;
    }

    @Override
    public void saveSupplier(Supplier supplier) {
        supplierRepository.save(supplier);
    }

    @Override
    public Supplier getSupplierById(Long id) throws RecordNotFoundException {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        if (supplierOptional.isPresent()) return supplierOptional.get();
        else throw new RecordNotFoundException("No Supplier record exists for id: " + id);
    }

    @Override
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
