package kitchenpos.domain.common;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.exception.InvalidPriceException;

@Embeddable
public class Price implements Comparable<Price> {

    public static final Price ZERO = new Price();

    @Column
    private final BigDecimal price;

    protected Price() {
        price = BigDecimal.ZERO;
    }

    public Price(final BigDecimal price) {
        validatePrice(price);

        this.price = price;
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException();
        }
    }

    public Price plus(final Price other) {
        return new Price(this.price.add(other.price));
    }

    public Price times(final long target) {
        return new Price(this.price.multiply(BigDecimal.valueOf(target)));
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(final Price other) {
        return this.price.compareTo(other.price);
    }
}
