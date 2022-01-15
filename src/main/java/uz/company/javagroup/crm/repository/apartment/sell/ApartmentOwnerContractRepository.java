package uz.isd.javagroup.grandcrm.repository.apartment.sell;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.isd.javagroup.grandcrm.entity.apartment.sell.ApartmentOwnerContract;

@Repository
public interface ApartmentOwnerContractRepository extends CrudRepository<ApartmentOwnerContract, Long> {
}
