package uz.isd.javagroup.grandcrm.Impl.apartment.sell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwner;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.apartment.sell.ApartmentOwnerRepository;
import uz.isd.javagroup.grandcrm.services.apartment.sell.ApartmentOwnerService;

import java.util.List;
import java.util.Optional;

@Service
public class ApartmentOwnerServiceImpl implements ApartmentOwnerService {

    @Autowired
    ApartmentOwnerRepository apartmentOwnerRepository;

    @Override
    public List<ApartmentOwner> findAll() {
        List<ApartmentOwner> apartmentOwnerList = (List<ApartmentOwner>) apartmentOwnerRepository.findAll();
        return apartmentOwnerList;
    }

    @Override
    public void saveApartmentOwner(ApartmentOwner apartmentOwner) {
        apartmentOwnerRepository.save(apartmentOwner);
    }

    @Override
    public ApartmentOwner getApartmentOwnerById(Long id) throws RecordNotFoundException {
        Optional<ApartmentOwner> apartmentOwnersOptional = apartmentOwnerRepository.findById(id);
        if (apartmentOwnersOptional.isPresent()) return apartmentOwnersOptional.get();
        else throw new RecordNotFoundException("No Apartment owner record exists for id: " + id);
    }

    @Override
    public void deleteApartmentOwner(Long id) {
        apartmentOwnerRepository.deleteById(id);
    }

}
