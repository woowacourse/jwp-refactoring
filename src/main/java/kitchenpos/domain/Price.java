package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

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
            throw new IllegalArgumentException("상품 가격은 null 혹은 음수가 될 수 없습니다.");
        }
    }

    public boolean isBigger(final BigDecimal otherPrice) {
        return value.compareTo(otherPrice) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
