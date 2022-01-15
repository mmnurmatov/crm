package uz.isd.javagroup.grandcrm.entity.apartment.sell;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "apartment_owner_contracts")
public class ApartmentOwnerContract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APARTMENTOWNER_CONTRACTS_SEQUENCE")
    @SequenceGenerator(sequenceName = "APARTMENTOWNER_CONTRACTS_SEQUENCE", allocationSize = 1, name = "APARTMENTOWNER_CONTRACTS_SEQUENCE")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_owner_id")
    @JsonIgnore
    ApartmentOwner apartmentOwner;

    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "floor")
    private int floor;

    @Column(name = "room_count")
    private int roomCount;

    @Column(name = "apartment_credit_payment_months")
    private int apartmentCreditPaymentMonths;

    @Column(name = "apartment_area")
    private BigDecimal apartmentArea;

    @Column(name = "contract_sum")
    private BigDecimal contractSum;

    @Column(name = "first_pay_sum")
    private BigDecimal firstPaySum;

    @Column(name = "first_pay_percent")
    private double firstPayPercent;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
