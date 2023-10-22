package kitchenpos.menu.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public final class Price implements Comparable {

    @Column(nullable = false, name = "price")
    private final BigDecimal value;

    public Price() {
        value = null;
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException();
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || getClass() != o.getClass()) {
            throw new IllegalArgumentException();
        }

        final Price otherPrice = (Price) o;

        return this.value.compareTo(otherPrice.getValue());
    }
}
