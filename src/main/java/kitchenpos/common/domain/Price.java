package kitchenpos.common.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.EmptyDataException;
import kitchenpos.common.exception.LowerThanZeroPriceException;

@Embeddable
public class Price {

    private static final BigDecimal PRICE_MIN_VALUE = BigDecimal.ZERO;

    @Column(name = "price")
    private BigDecimal value;

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    protected Price() {
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new EmptyDataException(Price.class.getSimpleName());
        }
        if (value.compareTo(PRICE_MIN_VALUE) < 0) {
            throw new LowerThanZeroPriceException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isHigher(BigDecimal productPriceSum) {
        return value.compareTo(productPriceSum) > 0;
    }
}
