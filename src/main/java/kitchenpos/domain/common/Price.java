package kitchenpos.domain.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.exception.InvalidPriceException;

@Embeddable
public class Price implements Comparable<Price> {

    public static final Price ZERO = new Price();

    @Column
    private final BigDecimal value;

    protected Price() {
        value = BigDecimal.ZERO;
    }

    public Price(final BigDecimal value) {
        validatePrice(value);

        this.value = value;
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
    }

    public Price plus(final Price other) {
        return new Price(this.value.add(other.value));
    }

    public Price times(final long target) {
        return new Price(this.value.multiply(BigDecimal.valueOf(target)));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public int compareTo(final Price other) {
        return this.value.compareTo(other.value);
    }
}
