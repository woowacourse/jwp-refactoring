package kitchenpos.domain;

import static kitchenpos.exception.PriceExceptionType.PRICE_CAN_NOT_NEGATIVE;
import static kitchenpos.exception.PriceExceptionType.PRICE_CAN_NOT_NULL;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.PriceException;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new PriceException(PRICE_CAN_NOT_NULL);
        }
        if (BigDecimal.ZERO.compareTo(value) > 0) {
            throw new PriceException(PRICE_CAN_NOT_NEGATIVE);
        }
    }

    public BigDecimal value() {
        return value;
    }
}
