package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO_PRICE = new Price(BigDecimal.ZERO);

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal price) {
        validateNull(price);
        validatePrice(price);
        this.value = price;
    }

    private static void validateNull(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("가격은 NULL일 수 없습니다. price: " + price);
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음수일 수 없습니다. price: " + price);
        }
    }

    public boolean isGreaterThan(final Price other) {
        return value.compareTo(other.value) > 0;
    }

    public Price multiplyByQuantity(final Long quantity) {
        validateQuantity(quantity);
        final BigDecimal result = value.multiply(BigDecimal.valueOf(quantity));

        return new Price(result);
    }

    private void validateQuantity(final Long quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("수량은 null이거나 음수일 수 없습니다.");
        }
    }

    public Price add(final Price other) {
        return new Price(value.add(other.value));
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
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
