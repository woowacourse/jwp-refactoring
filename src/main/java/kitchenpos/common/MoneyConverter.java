package kitchenpos.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import kitchenpos.vo.Money;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Long> {

    @Override
    public Long convertToDatabaseColumn(Money money) {
        return money.getAmount().longValue();
    }

    @Override
    public Money convertToEntityAttribute(Long amount) {
        return Money.valueOf(amount);
    }
}
