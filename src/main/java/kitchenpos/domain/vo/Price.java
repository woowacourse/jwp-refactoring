package kitchenpos.domain.vo;

import javax.persistence.Column;
import java.math.BigDecimal;

public class Price {

    @Column(name = "price")
    private BigDecimal value;

    public Price() {
    }

    public Price(final BigDecimal value) {
        validateMoreThanZero(value);
        this.value = value;
    }

    private void validateMoreThanZero(final BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public static Price of(final BigDecimal value) {
        return new Price(value);
    }

    public boolean isMoreThan(final Price otherPrice) {
        return value.compareTo(otherPrice.getValue()) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
