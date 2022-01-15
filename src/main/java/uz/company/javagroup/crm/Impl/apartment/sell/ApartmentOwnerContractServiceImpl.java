package uz.isd.javagroup.grandcrm.Impl.apartment.sell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwnerContract;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.apartment.sell.ApartmentOwnerContractRepository;
import uz.isd.javagroup.grandcrm.services.apartment.sell.ApartmentOwnerContractService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ApartmentOwnerContractServiceImpl implements ApartmentOwnerContractService {

    @Autowired
    ApartmentOwnerContractRepository apartmentOwnerContractRepository;


    @Override
    public List<ApartmentOwnerContract> findAll() {
        return (List<ApartmentOwnerContract>) apartmentOwnerContractRepository.findAll();
    }

    @Override
    public void saveApartmentOwnerContract(ApartmentOwnerContract apartmentOwnerContract) {
        apartmentOwnerContractRepository.save(apartmentOwnerContract);
    }

    @Override
    public ApartmentOwnerContract getApartmentOwnerContractById(Long id) throws RecordNotFoundException {
        Optional<ApartmentOwnerContract> apartmentOwnerContractOptional = apartmentOwnerContractRepository.findById(id);
        if (apartmentOwnerContractOptional.isPresent()) return apartmentOwnerContractOptional.get();
        else throw new RecordNotFoundException("No Apartment Owner Contract record exists for id: " + id);
    }

    @Override
    public void deleteApartmentOwnerContract(Long id) {
        apartmentOwnerContractRepository.deleteById(id);
    }
}
