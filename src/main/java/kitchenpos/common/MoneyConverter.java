package kitchenpos.common;

import java.math.BigDecimal;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import kitchenpos.vo.Money;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        return money.getAmount();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal amount) {
        return Money.valueOf(amount);
    }
}
