package kitchenpos.support;

import kitchenpos.domain.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money attribute) {
        return attribute.getValue();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal dbData) {
        return new Money(dbData);
    }
}
