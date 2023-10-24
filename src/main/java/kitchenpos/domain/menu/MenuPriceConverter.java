package kitchenpos.domain.menu;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;

public class MenuPriceConverter implements AttributeConverter<MenuPrice, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(MenuPrice attribute) {
        return attribute.getValue();
    }

    @Override
    public MenuPrice convertToEntityAttribute(BigDecimal dbData) {
        return new MenuPrice(dbData);
    }
}
