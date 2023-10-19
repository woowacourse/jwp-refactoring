package kitchenpos.domain.common;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Price {

    private static final Price ZERO = new Price(BigDecimal.ZERO);

    private BigDecimal price;

    protected Price() {
    }

    public Price(final BigDecimal price) {
        this.price = price;
    }

    public static Price zero() {
        return ZERO;
    }

    public boolean isUnderZero() {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    public Price add(final BigDecimal price) {
        return new Price(this.price.add(price));
    }

    public int compareTo(final Price target) {
        return price.compareTo(target.getPrice());
    }

    public BigDecimal getPrice() {
        return price;
    }
}
