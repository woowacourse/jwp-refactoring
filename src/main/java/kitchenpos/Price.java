package kitchenpos;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    public static final Price ZERO_PRICE = new Price(BigDecimal.ZERO);

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    private void validate(BigDecimal value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("가격은 존재해야 합니다.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0이상이어야 합니다.");
        }
    }

    public Price add(Price other) {
        return new Price(value.add(other.value));
    }

    public Price multiply(long other) {
        return new Price(value.multiply(BigDecimal.valueOf(other)));
    }

    public boolean isBiggerThan(Price other) {
        return value.compareTo(other.value) > 0;
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
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
