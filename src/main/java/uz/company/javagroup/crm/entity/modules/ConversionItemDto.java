package uz.isd.javagroup.grandcrm.entity.modules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionItemDto {
    private Long id;
    private Long conversionId;
    private Long parentId;
    private Long productId;
    private String productNameUz;
    private String productNameRu;
    private String productNameEn;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal amount;
    private List<ConversionItemDto> wrappedProducts;

    public ConversionItem convertToConversionItem() {
        return convertToConversionItem(new ConversionItem());
    }

    public ConversionItem convertToConversionItem(ConversionItem conversionItem) {
        if (id != null) conversionItem.setId(id);
        conversionItem.setCount(count);
        conversionItem.setPrice(price);
        conversionItem.setAmount(amount);
        return conversionItem;
    }
}
