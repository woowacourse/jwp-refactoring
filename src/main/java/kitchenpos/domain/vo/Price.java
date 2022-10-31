package kitchenpos.domain.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price", nullable = false)
    private BigDecimal value;

    protected Price() {
    }

    public Price(final BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public Price multiply(final long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }

    public Price add(final Price price) {
        return new Price(this.value.add(price.value));
    }

    public int compareTo(final Price price) {
        return this.value.compareTo(price.value);
    }

    private void validate(final BigDecimal value) {
        if (isNullOrNegative(value)) {
            throw new IllegalArgumentException("가격은 null 이거나 0원 미만일 수 없습니다.");
        }
    }

    private boolean isNullOrNegative(final BigDecimal value) {
        return Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0;
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
