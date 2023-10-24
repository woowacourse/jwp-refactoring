package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Money {

    private static final int MINIMUM_VALUE = 0;

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    protected Money() {
    }

    public static Money valueOf(BigDecimal value) {
        validatePrice(value);
        return new Money(value);
    }

    private static void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < MINIMUM_VALUE) {
            throw new IllegalArgumentException("가격은 " + MINIMUM_VALUE + " 미만일 수 없습니다.");
        }
    }

    public boolean isGreaterThan(BigDecimal price) {
        return value.compareTo(price) > 0;
    }

    public BigDecimal multiply(long number) {
        return value.multiply(BigDecimal.valueOf(number));
    }

    public BigDecimal getValue() {
        return value;
    }
}
