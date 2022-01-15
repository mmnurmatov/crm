package uz.isd.javagroup.grandcrm.services.apartment.sell;

import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwnerContract;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.util.List;

public interface ApartmentOwnerContractService {

    List<ApartmentOwnerContract> findAll();

    void saveApartmentOwnerContract(ApartmentOwnerContract apartmentOwnerContract);

    ApartmentOwnerContract getApartmentOwnerContractById(Long id) throws RecordNotFoundException;

    void deleteApartmentOwnerContract(Long id);

}
