package kitchenpos.domain.vo;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final int PRICE_SCALE = 2;
    private static final BigDecimal PRICE_MINIMUM = ZERO;
    private static final int PRICE_PRECISION_MAX = 19;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal value;

    protected Price() {
    }

    private Price(BigDecimal value) {
        validate(value);
        this.value = value.setScale(PRICE_SCALE, RoundingMode.DOWN);
    }

    public static Price from(final BigDecimal value) {
        return new Price(value);
    }

    private static void validate(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴 가격은 필수 항목입니다.");
        }
        if (price.compareTo(PRICE_MINIMUM) < 0) {
            throw new IllegalArgumentException("금액은 " + PRICE_MINIMUM + " 이상의 수여야 합니다.");
        }
        if (price.precision() > PRICE_PRECISION_MAX) {
            throw new IllegalArgumentException("금액은 최대 " + PRICE_PRECISION_MAX + "자리 수까지 가능합니다.");
        }
    }

    public static Price sum(final List<Price> values) {
        final BigDecimal sum = values.stream()
                .map(value -> value.value)
                .reduce(ZERO, BigDecimal::add);
        return Price.from(sum);
    }

    public boolean isBiggerThan(final Price other) {
        return value.compareTo(other.value) > 0;
    }

    public Price multiply(final long times) {
        return Price.from(value.multiply(BigDecimal.valueOf(times)));
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return Objects.equals(value, price1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
