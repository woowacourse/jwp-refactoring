package kitchenpos.price.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false, scale = 2)
    private BigDecimal value;

    protected Price() {
    }

    private Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public static Price from(final BigDecimal value) {
        final BigDecimal scaledPrice = value.setScale(2, RoundingMode.DOWN);
        return new Price(scaledPrice);
    }

    private static void validate(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴 가격은 필수 항목입니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
        }
    }

    public static Price sum(final List<Price> values) {
        final BigDecimal sum = values.stream()
                .map(value -> value.value)
                .reduce(ZERO, BigDecimal::add);
        return new Price(sum);
    }

    public boolean isLessThan(Price otherPrice) {
        return this.value.compareTo(otherPrice.value()) < 0;
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
