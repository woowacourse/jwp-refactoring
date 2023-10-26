package kitchenpos.global;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Price {

    @Column(name = "price")
    private BigDecimal value;

    protected Price() {
    }

    public Price(BigDecimal value) {
        validate(value);
        this.value = value;
    }

    public static Price zero() {
        return new Price(new BigDecimal(0));
    }

    private void validate(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("올바르지 않은 메뉴 가격입니다.");
        }
    }

    public Price multiply(Long n) {
        return new Price(value.multiply(BigDecimal.valueOf(n)));
    }

    public Price add(Price price) {
        return new Price(value.add(price.value));
    }

    public boolean isGreaterThan(Price other) {
        return value.compareTo(other.value) > 0;
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
        return value != null ? value.hashCode() : 0;
    }

    public BigDecimal getValue() {
        return value;
    }
}
