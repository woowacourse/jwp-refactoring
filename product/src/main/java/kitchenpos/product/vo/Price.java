package kitchenpos.product.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;

public class Price {

    private static final BigDecimal MINIMUM_VALUE = BigDecimal.ZERO;

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    private Price(BigDecimal value) {
        this.value = value;
    }

    protected Price() {
    }

    public static Price valueOf(BigDecimal value) {
        validateMinimum(value);
        return new Price(value);
    }

    private static void validateMinimum(BigDecimal price) {
        if (price.compareTo(MINIMUM_VALUE) < 0) {
            throw new IllegalArgumentException("상품 가격은 " + MINIMUM_VALUE + " 미만일 수 없습니다.");
        }
    }

    public Price multiply(long number) {
        return new Price(value.multiply(BigDecimal.valueOf(number)));
    }

    public BigDecimal getValue() {
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
        Price that = (Price) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
