package kitchenpos.product;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {
    @Column(precision = 10, scale = 2, name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validateValue(value);
        this.value = value;
    }

    public Price(int value) {
        this(BigDecimal.valueOf(value));
    }

    private void validateValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGreaterThan(Price price) {
        return this.value.compareTo(price.value) > 0;
    }

    public Price multiply(Long quantity) {
        return new Price(this.value.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price add(Price price) {
        return new Price(this.value.add(price.value));
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
        return Objects.equals(getValue(), price.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
