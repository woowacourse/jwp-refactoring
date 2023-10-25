package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
        this.value = null;
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(final BigDecimal value) {
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price value Should over 0");
        }
    }

    public Price add(final Price target) {
        return new Price(value.add(target.value));
    }

    public Price multiply(final BigDecimal multiplyValue) {
        return new Price(value.multiply(multiplyValue));
    }

    public boolean isLargerThan(final Price target) {
        return value.compareTo(target.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
