package uz.isd.javagroup.grandcrm.entity.modules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseProductDto {
    private Long id;
    private Long fromWarehouseId;
    private String fromWarehouseName;
    private Long toWarehouseId;
    private String toWarehouseName;
    private String regNumber;
    private String reason;
    private BigDecimal summa;
    private Date createdAt;
    private Date updatedAt;
    private Long linkedWarehouseId;
    private Long linkedWarehouseRequestId;
    private String linkedWarehouseRequestRegNumber;
    private WarehouseProduct.DocumentType documentType;
    private WarehouseProduct.Action action;
    private WarehouseProduct.DocumentStatus documentStatus;
    private List<WarehouseProductItemDto> whItems;
}
