package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final Price DEFAULT_PRICE = new Price();

    private BigDecimal price;

    protected Price() {
        this.price = BigDecimal.ZERO;
    }

    private Price(final BigDecimal price) {
        validate(price);
        this.price = price;
    }

    public static Price from(final BigDecimal price) {
        return new Price(price);
    }

    public static Price from(final Long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    private static void validate(final BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("Price must not be null.");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
    }

    public static Price getDefault() {
        return DEFAULT_PRICE;
    }

    public Price multiply(final long quantity) {
        return new Price(price.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price add(Price other) {
        return new Price(this.price.add(other.price));
    }

    public boolean isGreaterThan(final Price other) {
        return this.price.compareTo(other.price) > 0;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final Price price1 = (Price) object;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal getValue() {
        return price;
    }
}
