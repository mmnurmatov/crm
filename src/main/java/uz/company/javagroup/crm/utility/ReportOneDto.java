package uz.isd.javagroup.grandcrm.utility;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportOneDto {
    private Long   productId;
    private String productNameUz;
    private String productNameRu;
    private String productNameEn;

    private Long   unitId;
    private String unitNameUz;
    private String unitNameRu;
    private String unitNameEn;

    private BigDecimal IncomeCount;
    private BigDecimal IncomePrice;

    private BigDecimal ExpanseCount;
    private BigDecimal ExpansePrice;
}
