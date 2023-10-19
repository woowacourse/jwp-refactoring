package kitchenpos.domain;

import static kitchenpos.domain.exception.PriceExceptionType.PRICE_IS_LOWER_THAN_ZERO;
import static kitchenpos.domain.exception.PriceExceptionType.PRICE_IS_NULL;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;
import kitchenpos.domain.exception.PriceException;

@Embeddable
public class Price {

    private final BigDecimal value;

    protected Price() {
        this(null);
    }

    public Price(final BigDecimal value) {
        validatePrice(value);
        this.value = value;
    }

    private void validatePrice(final BigDecimal value) {
        if (value == null) {
            throw new PriceException(PRICE_IS_NULL);
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceException(PRICE_IS_LOWER_THAN_ZERO);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
