package kitchenpos.domain.vo;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final int PRICE_SCALE = 2;
    private static final BigDecimal PRICE_MINIMUM = ZERO;
    private static final int PRICE_PRECISION_MAX = 19;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price from(final BigDecimal value) {
        validate(value);
        final BigDecimal scaledPrice = value.setScale(PRICE_SCALE, RoundingMode.DOWN);
        return new Price(scaledPrice);
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
                .map(value -> value.price)
                .reduce(ZERO, BigDecimal::add);
        return Price.from(sum);
    }

    public boolean isBiggerThan(final Price other) {
        return price.compareTo(other.price) > 0;
    }

    public Price multiply(final long times) {
        return Price.from(price.multiply(BigDecimal.valueOf(times)));
    }

    public BigDecimal value() {
        return price;
    }
}
