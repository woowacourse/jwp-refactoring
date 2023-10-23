package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.valueOf(0));

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        this.value = value;
    }

    public Price add(final Price otherPrice) {
        return new Price(value.add(otherPrice.value));
    }

    public Price multiply(final long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean isGreaterThan(final Price price) {
        return value.compareTo(price.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
