package kitchenpos.domain.product;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;

public class ProductPriceConverter implements AttributeConverter<ProductPrice, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(ProductPrice attribute) {
        return attribute.getValue();
    }

    @Override
    public ProductPrice convertToEntityAttribute(BigDecimal dbData) {
        return new ProductPrice(dbData);
    }
}
