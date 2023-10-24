package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.exception.InvalidPriceException;

public class Price {
    private final BigDecimal value;

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("가격은 null이거나 음수일 수 없습니다.");
        }
    }

    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
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
