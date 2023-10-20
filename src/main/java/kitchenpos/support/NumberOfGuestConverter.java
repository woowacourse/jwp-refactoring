package kitchenpos.support;

import kitchenpos.domain.vo.NumberOfGuest;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class NumberOfGuestConverter implements AttributeConverter<NumberOfGuest, Integer> {

    @Override
    public Integer convertToDatabaseColumn(NumberOfGuest attribute) {
        return attribute.getValue();
    }

    @Override
    public NumberOfGuest convertToEntityAttribute(Integer dbData) {
        return new NumberOfGuest(dbData);
    }
}
