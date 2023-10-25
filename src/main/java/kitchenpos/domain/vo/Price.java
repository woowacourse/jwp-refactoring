package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    public static final Price ZERO = new Price(BigDecimal.ZERO);

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("가격은 null 일 수 없습니다.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음이 아닌 정수이어야 합니다.");
        }
        this.value = value;
    }

    public static Price from(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("가격은 null 일 수 없습니다.");
        }
        final BigDecimal newValue = new BigDecimal(value);
        if (newValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 음이 아닌 정수이어야 합니다.");
        }

        return new Price(newValue);
    }

    public Price sum(final Price otherPrice) {
        return new Price(value.add(otherPrice.value));
    }

    public Price multiply(final Quantity quantity) {
        return new Price(value.multiply(new BigDecimal(quantity.getValue())));
    }

    public boolean isGreaterThan(final Price otherPrice) {
        return value.compareTo(otherPrice.value) == 1;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
