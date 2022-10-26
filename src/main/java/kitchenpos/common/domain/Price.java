package kitchenpos.common.domain;

import java.math.BigDecimal;
import kitchenpos.common.exception.BadPriceCreatedException;

public class Price {

    private static final int MINIMUM_PRICE = 0;

    private final BigDecimal value;

    private Price(BigDecimal value) {
        this.value = value;
    }

    public static Price of(BigDecimal price) {
        return new Price(price);
    }

    public static Price valueOf(Integer price) {
        validate(price);
        return new Price(BigDecimal.valueOf(price));
    }
    
    private static void validate(Integer value) {
        if (shouldNotPriceNumber(value)) {
            throw new BadPriceCreatedException();
        }
    }

    private static boolean shouldNotPriceNumber(Integer value) {
        return value == null || value < MINIMUM_PRICE;
    }

    public BigDecimal getValue() {
        return value;
    }
}
