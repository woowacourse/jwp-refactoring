package kitchenpos.domain.Product;

import java.math.BigDecimal;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidPriceException;

@Embeddable
public class Price {

    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        validatePositive(value);
        validateScaleLessThanMaximum(value);
    }

    private void validatePositive(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException("가격은 0원 이상이어야 합니다.");
        }
    }

    private void validateScaleLessThanMaximum(final BigDecimal value) {
        if (value.scale() > 2) {
            throw new InvalidPriceException("가격은 소수 부분은 2자리까지만 입력 가능합니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
