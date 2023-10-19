package kitchenpos.refactoring.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private final BigDecimal price;

    public Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    private void validate(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Price add(Price addend) {
        BigDecimal result = price.add(addend.price);
        return new Price(result);
    }

    public Price multiply(long quantity) {
        BigDecimal result = price.multiply(BigDecimal.valueOf(quantity));

        return new Price(result);
    }

    public boolean isGreaterThan(Price other) {
        return price.compareTo(other.price) > 0;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
