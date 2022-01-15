package uz.isd.javagroup.grandcrm.repository.apartment.sell;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwner;

@Repository
public interface ApartmentOwnerRepository extends CrudRepository<ApartmentOwner, Long> {

}
