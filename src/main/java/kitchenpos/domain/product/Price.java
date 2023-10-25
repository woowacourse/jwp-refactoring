package kitchenpos.domain.product;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal value;

    public Price(long value) {
        this(BigDecimal.valueOf(value));
    }

    public Price(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 한다.");
        }
        this.value = value;
    }

    public static Price zero() {
        return new Price(0L);
    }

    public boolean isBigger(Price price) {
        return value.compareTo(price.getValue()) > 0;
    }

    public Price multiplyWithQuantity(long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price add(Price price) {
        return new Price(value.add(price.value));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
