package kitchenpos.common.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final int LESS_THAN_PARAMETER = -1;
    private static final int EQUAL_PARAMETER = 0;
    private static final int GREATER_THAN_PARAMETER = 1;

    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(nullable = false)
    private BigDecimal price;

    protected Price() {
    }

    private Price(BigDecimal price) {
        this.price = price;
    }

    public static Price from(BigDecimal price) {
        validateNull(price);
        validateNegative(price);

        return new Price(price);
    }

    private static void validateNull(Object price) {
        if (Objects.nonNull(price)) {
            return;
        }

        throw new IllegalArgumentException();
    }

    public static Price from(Long value) {
        validateNull(value);
        BigDecimal price = BigDecimal.valueOf(value);

        validateNegative(price);

        return new Price(price);
    }

    private static void validateNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) <= LESS_THAN_PARAMETER) {
            throw new IllegalArgumentException();
        }
    }

    public Price add(Price other) {
        return Price.from(
                price.add(other.getValue())
        );
    }

    public boolean moreExpensiveThan(Price other) {
        return price.compareTo(other.getValue()) >= GREATER_THAN_PARAMETER;
    }

    public static Price getFreePrice() {
        return Price.from(BigDecimal.ZERO);
    }

    public BigDecimal getValue() {
        return price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        return price.compareTo(((Price) other).price) == EQUAL_PARAMETER;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

}
