package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column(name = "price")
    private final BigDecimal value;

    protected Price() {
        this.value = BigDecimal.ZERO;
    }

    public Price(final BigDecimal value) {
        validateNotNegative(value);
        this.value = value;
    }

    private static void validateNotNegative(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Price init() {
        return new Price();
    }

    public Price multiply(final long multiplier) {
        return new Price(value.multiply(BigDecimal.valueOf(multiplier)));
    }

    public Price add(final Price price) {
        return new Price(this.value.add(price.value));
    }

    public boolean biggerThan(final Price price) {
        return this.value.compareTo(price.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
