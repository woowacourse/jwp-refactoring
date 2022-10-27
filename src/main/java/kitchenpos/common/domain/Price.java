package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.common.exception.BadPriceCreatedException;

public class Price {

    private static final int MINIMUM_PRICE = 0;

    private final BigDecimal value;

    private Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public static Price from(BigDecimal price) {
        return new Price(price);
    }

    public static Price from(Integer price) {
        return new Price(BigDecimal.valueOf(price));
    }
    
    private static void validate(BigDecimal value) {
        if (shouldNotPriceNumber(value)) {
            throw new BadPriceCreatedException();
        }
    }

    private static boolean shouldNotPriceNumber(BigDecimal value) {
        return value == null || value.intValue() < MINIMUM_PRICE;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
