package uz.isd.javagroup.grandcrm.services.apartment.sell;

import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwner;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.io.IOException;
import java.util.List;

public interface ApartmentOwnerService {

    List<ApartmentOwner> findAll();

    void saveApartmentOwner(ApartmentOwner apartmentOwner);

    ApartmentOwner getApartmentOwnerById(Long id) throws RecordNotFoundException;

    void deleteApartmentOwner(Long id);
}
