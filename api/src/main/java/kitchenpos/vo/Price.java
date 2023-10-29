package kitchenpos.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    private static final BigDecimal MIN = BigDecimal.ZERO;
    public static final Price ZERO = Price.from(BigDecimal.ZERO);

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {}

    private Price(final BigDecimal value) {
        this.value = value;
    }

    public static Price from(final BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("가격은 비어있을 수 없습니다.");
        }
        if (value.compareTo(MIN) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }
        return new Price(value);
    }

    public boolean isGreaterThan(final Price other) {
        return this.value.compareTo(other.value) > 0;
    }

    public Price multiply(final BigDecimal other) {
        return Price.from(this.value.multiply(other));
    }

    public Price add(final Price other) {
        return Price.from(this.value.add(other.value));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Price price1 = (Price) o;
        return value.equals(price1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
