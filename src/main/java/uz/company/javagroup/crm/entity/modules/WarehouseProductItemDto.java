package uz.isd.javagroup.grandcrm.entity.modules;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.isd.javagroup.grandcrm.entity.directories.ProductDto;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseProductItemDto {
    private Long id;
    private Long warehouseProductId;
    private Long productId;
    private ProductDto productDto;
    private Long sectorId;
    private BigDecimal count;
    private BigDecimal remaining;
    private BigDecimal incomePrice;
    private BigDecimal price;
    private BigDecimal priceWholesale;
    private Date createdAt;
    private Date updatedAt;
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date productionDate;
    @Temporal(value = TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expireDate;
}
