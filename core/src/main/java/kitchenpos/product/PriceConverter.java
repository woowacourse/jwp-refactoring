package kitchenpos.product;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;

public class PriceConverter implements AttributeConverter<Price, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Price attribute) {
        return attribute.getValue();
    }

    @Override
    public Price convertToEntityAttribute(BigDecimal dbData) {
        return new Price(dbData);
    }
}
