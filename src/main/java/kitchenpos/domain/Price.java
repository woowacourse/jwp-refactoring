package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    private static final int ZERO = 0;

    @Column(nullable = false, name = "price")
    private BigDecimal value;

    private Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price from(final Integer price) {
        validateValue(price);
        return new Price(new BigDecimal(price));
    }

    protected Price() {

    }

    private static void validateValue(final Integer value) {
        if (Objects.isNull(value) || value < ZERO) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isBigger(final BigDecimal otherPrice) {
        return value.compareTo(otherPrice) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
