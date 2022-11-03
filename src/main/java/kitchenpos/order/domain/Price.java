package kitchenpos.order.domain;

import static kitchenpos.exception.ExceptionType.INVALID_PRICE_EXCEPTION;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.CustomIllegalArgumentException;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    public Price() {
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomIllegalArgumentException(INVALID_PRICE_EXCEPTION);
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
