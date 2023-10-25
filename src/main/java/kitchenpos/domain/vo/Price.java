package kitchenpos.domain.vo;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final int MIN_PRICE = 0;

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < MIN_PRICE) {
            throw new IllegalArgumentException("가격은 0이상 이어야 합니다.");
        }
    }

    public boolean isGreaterThan(final BigDecimal other) {
        return value.compareTo(other) > 0;
    }

    public BigDecimal multiply(final long other) {
        return value.multiply(BigDecimal.valueOf(other));
    }

    public BigDecimal getValue() {
        return value;
    }
}
